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
public class PopularCommandTest {

	@Mock
	private static MovieClient client;
	private List<MovieBasic> movieList;

	@Before
	public void set() {
		UsersInfo.INSTANCE.clearInfo();
		MovieReferences.clearAllReferences();
	}

	@Test
	public void testPopularCommand() throws MovieDbException {
		MovieBasic movie = new MovieBasic();
		movie.setTitle("alien");

		movieList = new ArrayList<MovieBasic>();
		movieList.add(movie);

		UserMovies ui = UsersInfo.INSTANCE.getUserMovies(1);
		List<MovieReference> excluded = ui.watched();
		excluded.addAll(ui.blacklisted());
		excluded.addAll(ui.watchlist());

		when(client.retrievePopulars(1, excluded)).thenReturn(movieList);

		BotCommand c = new PopularCommand(client, new BasicFormatter());
		assertEquals("The most popular movies at the moment are :\n- alien\n", c.response(createMockMessage(1, 1, "/popular 1")));
	}

	@Test
	public void testPopularNoSizeCommand() throws MovieDbException {
		final int DEFAULT_SIZE = 10;

		MovieBasic movie = new MovieBasic();
		movie.setTitle("alien");

		movieList = new ArrayList<MovieBasic>();
		movieList.add(movie);

		UserMovies ui = UsersInfo.INSTANCE.getUserMovies(1);
		List<MovieReference> excluded = ui.watched();
		excluded.addAll(ui.blacklisted());
		excluded.addAll(ui.watchlist());

		when(client.retrievePopulars(DEFAULT_SIZE, excluded)).thenReturn(movieList);

		BotCommand c = new PopularCommand(client, new BasicFormatter());
		assertEquals("The most popular movies at the moment are :\n- alien\n", c.response(createMockMessage(1, 1, "/popular")));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidParameter() throws Exception {
		new PopularCommand(client, new BasicFormatter()).response(createMockMessage(1, 1, "/popular BAD"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTooManyParameters() throws Exception {
		new PopularCommand(client, new BasicFormatter()).response(createMockMessage(1, 1, "/popular 10 BAD"));
	}

	@Test
	public void testDescriptionAndHook() throws Exception {
		BotCommand c = new PopularCommand(client, new BasicFormatter());
		assertEquals("popular. Parameters: [n]. Suggests n (default: 10. max: 25) films for the user to watch.",
				c.hook() + ". " + c.description());
	}

	private Message createMockMessage(int userID, long chatID, String text) {
		String msg = UpdateBuilder.getMessage("pippo", userID, chatID, text, 1);
		Update update = BotUtils.parseUpdate(msg);
		return update.message();
	}
}