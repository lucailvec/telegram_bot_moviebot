package it.unimi.di.sweng.moviebot.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
public class RecentCommandTest {

	@Mock
	private static MovieClient client;
	private List<MovieBasic> movieList;

	@Before
	public void set() {
		UsersInfo.INSTANCE.clearInfo();
		MovieReferences.clearAllReferences();
	}

	@Test
	public void testRecentCommand() throws MovieDbException {

		MovieBasic movie = new MovieBasic();
		movie.setTitle("alien");
		movie.setReleaseDate("06-06-1956");
		movieList = new ArrayList<MovieBasic>();
		movieList.add(movie);

		UserMovies ui = UsersInfo.INSTANCE.getUserMovies(1);
		List<MovieReference> excluded = ui.watched();
		excluded.addAll(ui.blacklisted());
		excluded.addAll(ui.watchlist());

		DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate date = LocalDate.parse("05-06-2016", DEFAULT_FORMATTER);

		when(client.retrievePopularsAfter(date, 1, excluded)).thenReturn(movieList);

		BotCommand c = new RecentCommand(client, new BasicFormatter());
		assertEquals("Popular movies released after the given date are :\n- alien\n", c.response(createMockMessage(1, 1, "/recent 05-06-2016 1")));
	}

	@Test
	public void testRecentNoSizeCommand() throws MovieDbException {
		final int DEFAULT_SIZE = 10;

		MovieBasic movie = new MovieBasic();
		movie.setTitle("alien");
		movie.setReleaseDate("06-06-1956");
		movieList = new ArrayList<MovieBasic>();
		movieList.add(movie);

		UserMovies ui = UsersInfo.INSTANCE.getUserMovies(1);
		List<MovieReference> excluded = ui.watched();
		excluded.addAll(ui.blacklisted());
		excluded.addAll(ui.watchlist());

		DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate date = LocalDate.parse("05-06-2016", DEFAULT_FORMATTER);

		when(client.retrievePopularsAfter(date, DEFAULT_SIZE, excluded)).thenReturn(movieList);

		BotCommand c = new RecentCommand(client, new BasicFormatter());
		assertEquals("Popular movies released after the given date are :\n- alien\n", c.response(createMockMessage(1, 1, "/recent 05-06-2016")));
	}

	@Test
	public void testDescriptionAndHook() throws Exception {
		BotCommand c = new RecentCommand(client, new BasicFormatter());

		assertEquals(
				"recent. Paramteres: {ReleaseDate} [n]. Suggests n (default: 10. max: 25) films that were released in or after the given date for the user to watch.",
				c.hook() + ". " + c.description());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMissingParameters() throws Exception {
		new RecentCommand(client, new BasicFormatter()).response(createMockMessage(1, 1, "/recent"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTooParameters() throws Exception {
		new RecentCommand(client, new BasicFormatter())
				.response(createMockMessage(1, 1, "/recent 05-06-2015 06-06-2015 5"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidDate() throws Exception {
		new RecentCommand(client, new BasicFormatter()).response(createMockMessage(1, 1, "/recent BAD"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidMovieNumber() throws Exception {
		new RecentCommand(client, new BasicFormatter()).response(createMockMessage(1, 1, "/recent 06-06-2015 BAD"));
	}

	private Message createMockMessage(int userID, long chatID, String text) {
		String msg = UpdateBuilder.getMessage("pippo", userID, chatID, text, 1);
		Update update = BotUtils.parseUpdate(msg);
		return update.message();
	}
}
