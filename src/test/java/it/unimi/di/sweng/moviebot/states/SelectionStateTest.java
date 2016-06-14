package it.unimi.di.sweng.moviebot.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.yamj.api.common.exception.ApiExceptionType;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;

import it.unimi.di.sweng.moviebot.client.MovieClientFactory;
import it.unimi.di.sweng.moviebot.commands.MovieSearchCommand;
import it.unimi.di.sweng.moviebot.commands.OverviewCommand;
import it.unimi.di.sweng.moviebot.commands.TrailerCommand;
import it.unimi.di.sweng.moviebot.formatter.BasicFormatter;
import it.unimi.di.sweng.moviebot.model.UsersInfo;
import it.unimi.di.sweng.moviebot.server.UpdateBuilder;

public class SelectionStateTest {

	private static BotState state;
	private static TheMovieDbApi mockApi = mock(TheMovieDbApi.class);
	private static MovieSearchCommand command;

	@Before
	public void setUp() {
		command = new OverviewCommand(new MovieClientFactory(mockApi).createClient(), new BasicFormatter());
	}

	@Test
	public void testValidSelection() throws Exception {
		state = new SelectionState(Arrays.asList(createMockMovie(1, "mymovie", "myoverview"),
				createMockMovie(2, "mymovie2", "myoverview2")), command);

		UsersInfo.INSTANCE.setChatState(1, state);

		assertEquals("About the movie :\nmyoverview",
				state.handle(Logger.getLogger(Logger.GLOBAL_LOGGER_NAME), createMockMessage(1, 1, "/1")));

		assertEquals("InitialState", UsersInfo.INSTANCE.getChatState(1).getClass().getSimpleName());
	}

	@Test
	public void testOutOfBoundsSelection() throws Exception {
		state = new SelectionState(Arrays.asList(new MovieInfo(), new MovieInfo()), command);

		UsersInfo.INSTANCE.setChatState(1, state);

		assertEquals("Please enter a number between 1 and 2",
				state.handle(Logger.getLogger(Logger.GLOBAL_LOGGER_NAME), createMockMessage(1, 1, "/3")));

		assertEquals(state, UsersInfo.INSTANCE.getChatState(1));
	}

	@Test
	public void testInvalidCommand() throws Exception {
		state = new SelectionState(Arrays.asList(new MovieInfo(), new MovieInfo()), command);

		UsersInfo.INSTANCE.setChatState(1, state);

		assertEquals("Please enter a number between 1 and 2",
				state.handle(Logger.getLogger(Logger.GLOBAL_LOGGER_NAME), createMockMessage(1, 1, "/BAD")));

		assertEquals(state, UsersInfo.INSTANCE.getChatState(1));
	}

	@Test
	public void testCancelCommand() throws Exception {
		state = new SelectionState(Arrays.asList(new MovieInfo(), new MovieInfo()), null);

		UsersInfo.INSTANCE.setChatState(1, state);

		assertEquals("Selection has been canceled.",
				state.handle(Logger.getLogger(Logger.GLOBAL_LOGGER_NAME), createMockMessage(1, 1, "/cancel")));

		assertEquals("InitialState", UsersInfo.INSTANCE.getChatState(1).getClass().getSimpleName());
	}

	@Test
	public void testMovieDbError() throws Exception {
		command = new TrailerCommand(new MovieClientFactory(mockApi).createClient(), new BasicFormatter());

		when(mockApi.getMovieVideos(1, "en"))
				.thenThrow(new MovieDbException(ApiExceptionType.CONNECTION_ERROR, "myerror"));

		MovieInfo m = new MovieInfo();
		m.setId(1);

		state = new SelectionState(Arrays.asList(m, new MovieInfo()), command);

		UsersInfo.INSTANCE.setChatState(1, state);

		assertEquals(
				"There was a problem with TheMovieDB, please try again later.\n"
						+ "ExceptionType=CONNECTION_ERROR, ResponseCode=0, URL=",
				state.handle(Logger.getLogger(Logger.GLOBAL_LOGGER_NAME), createMockMessage(1, 1, "/1")));

		assertEquals(state, UsersInfo.INSTANCE.getChatState(1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTooFewChoices() throws Exception {
		state = new SelectionState(Arrays.asList(new MovieInfo()), command);
	}

	@Test
	public void testEquals() throws Exception {
		MovieInfo m1 = new MovieInfo();
		m1.setId(1);
		MovieInfo m2 = new MovieInfo();
		m2.setId(2);
		MovieInfo m3 = new MovieInfo();
		m3.setId(3);

		state = new SelectionState(Arrays.asList(m1, m2), command);
		SelectionState state1 = new SelectionState(Arrays.asList(m1, m2), command);
		SelectionState state2 = new SelectionState(Arrays.asList(m1, m3), command);
		SelectionState state3 = new SelectionState(Arrays.asList(m1, m2),
				new TrailerCommand(new MovieClientFactory().createClient(), new BasicFormatter()));

		assertEquals(state, state1);
		assertNotEquals(state, state2);
		assertNotEquals(state, state3);
	}

	private Message createMockMessage(int userID, long chatID, String text) {
		String msg = UpdateBuilder.getMessage("pippo", userID, chatID, text, 1);
		Update update = BotUtils.parseUpdate(msg);
		return update.message();
	}

	private MovieInfo createMockMovie(int id, String title, String overview) {
		MovieInfo movie = new MovieInfo();
		movie.setId(id);
		movie.setTitle(title);
		movie.setOverview(overview);
		return movie;
	}
}