package it.unimi.di.sweng.moviebot.commands;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.model.movie.MovieBasic;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;

import it.unimi.di.sweng.moviebot.client.MovieClient;
import it.unimi.di.sweng.moviebot.formatter.BasicFormatter;
import it.unimi.di.sweng.moviebot.model.MovieReferences;
import it.unimi.di.sweng.moviebot.model.MovieReferences.MovieReference;
import it.unimi.di.sweng.moviebot.server.UpdateBuilder;
import it.unimi.di.sweng.moviebot.model.UserMovies;
import it.unimi.di.sweng.moviebot.model.UsersInfo;

@RunWith(MockitoJUnitRunner.class)
public class GenreCommandTest {

	@Mock
	private static MovieClient client;
	private List<MovieBasic> movieList;

	@Before
	public void set() {
		UsersInfo.INSTANCE.clearInfo();
		MovieReferences.clearAllReferences();
	}

	@Test
	public void testGenreCommand() throws MovieDbException {
		MovieBasic movie = new MovieBasic();
		movie.setTitle("alien");
		movie.setId(1);
		movie.setGenreIds(new ArrayList<Integer>(1));

		movieList = new ArrayList<MovieBasic>();
		movieList.add(movie);

		UserMovies ui = UsersInfo.INSTANCE.getUserMovies(1);
		List<MovieReference> excluded = ui.watched();
		excluded.addAll(ui.blacklisted());
		excluded.addAll(ui.watchlist());

		when(client.retrievePopularsByGenre("horror", 10, excluded)).thenReturn(movieList);

		BotCommand c = new GenreCommand(client, new BasicFormatter());
		assertEquals("Popular movies for the given genre are :\n- alien\n", c.response(createMockMessage(1, 1, "/genre horror")));
	}

	@Test
	public void testComposedGenreCommand() throws MovieDbException {
		MovieBasic movie = new MovieBasic();
		movie.setTitle("alien");
		movie.setId(1);
		movie.setGenreIds(new ArrayList<Integer>(1));

		movieList = new ArrayList<MovieBasic>();
		movieList.add(movie);

		UserMovies ui = UsersInfo.INSTANCE.getUserMovies(1);
		List<MovieReference> excluded = ui.watched();
		excluded.addAll(ui.blacklisted());
		excluded.addAll(ui.watchlist());

		when(client.retrievePopularsByGenre("science fiction", 1, excluded)).thenReturn(movieList);

		BotCommand c = new GenreCommand(client, new BasicFormatter());
		assertEquals("Popular movies for the given genre are :\n- alien\n", c.response(createMockMessage(1, 1, "/genre science fiction 1")));

	}

	@Test
	public void testNoSizeCommand() throws MovieDbException {

		final int DEFAULT_SIZE = 10;

		MovieBasic movie = new MovieBasic();
		movie.setTitle("alien");
		movie.setId(1);
		movie.setGenreIds(new ArrayList<Integer>(1));

		movieList = new ArrayList<MovieBasic>();
		movieList.add(movie);

		UserMovies ui = UsersInfo.INSTANCE.getUserMovies(1);
		List<MovieReference> excluded = ui.watched();
		excluded.addAll(ui.blacklisted());
		excluded.addAll(ui.watchlist());

		when(client.retrievePopularsByGenre("science fiction", DEFAULT_SIZE, excluded)).thenReturn(movieList);

		BotCommand c = new GenreCommand(client, new BasicFormatter());
		assertEquals("Popular movies for the given genre are :\n- alien\n", c.response(createMockMessage(1, 1, "/genre science fiction")));

	}

	@Test
	public void testDescriptionAndHook() throws Exception {
		BotCommand c = new GenreCommand(client, new BasicFormatter());

		assertEquals(
				"genre. Parameters: {FilmName} [n]. Suggests n (default: 10. max: 25) films of the given genre for the user to watch.",
				c.hook() + ". " + c.description());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMissingParameters() throws Exception {
		new GenreCommand(client, new BasicFormatter()).response(createMockMessage(1, 1, "/genre"));
	}

	private Message createMockMessage(int userID, long chatID, String text) {
		String msg = UpdateBuilder.getMessage("pippo", userID, chatID, text, 1);
		Update update = BotUtils.parseUpdate(msg);
		return update.message();
	}
}
