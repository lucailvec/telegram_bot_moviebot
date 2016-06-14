package it.unimi.di.sweng.moviebot.commands;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;

import it.unimi.di.sweng.moviebot.client.MovieClient;
import it.unimi.di.sweng.moviebot.formatter.BasicFormatter;
import it.unimi.di.sweng.moviebot.model.UsersInfo;
import it.unimi.di.sweng.moviebot.server.UpdateBuilder;
import it.unimi.di.sweng.moviebot.states.BotState;
import it.unimi.di.sweng.moviebot.states.SelectionState;

public class MovieSearchCommandTest {

	private MovieClient client = mock(MovieClient.class);
	private MyCommand command = new MyCommand(client);

	private class MyCommand extends MovieSearchCommand {

		public MyCommand(MovieClient client) {
			super(client, new BasicFormatter());
		}

		@Override
		public String hook() {
			return null;
		}

		@Override
		public String description() {
			return null;
		}

		@Override
		public String response(int userID, MovieInfo movie) throws MovieDbException {
			return movie.getTitle();
		}

	}

	@Test
	public void testSearchMovie() throws Exception {
		MovieInfo m = new MovieInfo();
		m.setOverview("good film");
		m.setTitle("alien");
		m.setId(1);
		List<MovieInfo> mf = new ArrayList<MovieInfo>();
		mf.add(m);

		when(client.searchMovie("alien", 10)).thenReturn(mf);

		assertEquals("alien", command.response(createMockMessage(1, 1, "/mycommand alien")));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNotFound() throws Exception {
		MovieInfo m = new MovieInfo();
		m.setOverview("good film");
		m.setTitle("alien 2");
		m.setId(1);
		List<MovieInfo> mf = new ArrayList<MovieInfo>();
		mf.add(m);

		when(client.searchMovie("alien", 10)).thenReturn(mf);

		command.response(createMockMessage(1, 1, "/mycommand alien"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNotFound2() throws Exception {
		when(client.searchMovie("alien", 10)).thenReturn(new ArrayList<MovieInfo>());

		command.response(createMockMessage(1, 1, "/mycommand alien"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMissingParameters() throws Exception {
		command.response(createMockMessage(1, 1, "/mycommand"));
	}

	@Test
	public void testMultipleSearchResults() throws Exception {
		MovieInfo m = new MovieInfo();
		m.setTitle("myfilm");
		m.setReleaseDate("2015");

		MovieInfo m2 = new MovieInfo();
		m2.setTitle("myfilm2");
		m2.setReleaseDate("2016");

		when(client.searchMovie("myfilm", 10)).thenReturn(Arrays.asList(m, m2));

		assertEquals("Search results:\n" + "1) myfilm (2015)\n" + "2) myfilm2 (2016)\n",
				command.response(createMockMessage(1, 1, "/mycommand myfilm")));

		BotState state = new SelectionState(Arrays.asList(m, m2), command);

		assertEquals(state, UsersInfo.INSTANCE.getChatState(1));
	}

	private Message createMockMessage(int userID, long chatID, String text) {
		String msg = UpdateBuilder.getMessage("pippo", userID, chatID, text, 1);
		Update update = BotUtils.parseUpdate(msg);
		return update.message();
	}
}
