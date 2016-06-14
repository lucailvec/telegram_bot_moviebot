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
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;

import it.unimi.di.sweng.moviebot.client.MovieClient;
import it.unimi.di.sweng.moviebot.formatter.BasicFormatter;
import it.unimi.di.sweng.moviebot.model.MovieReferences;
import it.unimi.di.sweng.moviebot.model.UsersInfo;
import it.unimi.di.sweng.moviebot.server.UpdateBuilder;

@RunWith(MockitoJUnitRunner.class)
public class UnwatchCommandTest {

	@Mock
	private static MovieClient client;
	private List<MovieInfo> mf;

	@Before
	public void set() {
		UsersInfo.INSTANCE.clearInfo();
		MovieReferences.clearAllReferences();
	}

	@Test
	public void testUnwatchCommand() throws MovieDbException {

		UsersInfo.INSTANCE.watch(1, MovieReferences.getMovieReference(1, "alien"));
		;

		MovieInfo m = new MovieInfo();
		m.setOverview("good film");
		m.setTitle("alien");
		m.setId(1);
		mf = new ArrayList<MovieInfo>();
		mf.add(m);

		when(client.searchMovie("alien", 10)).thenReturn(mf);

		assertEquals(1, UsersInfo.INSTANCE.getUserMovies(1).watched().size());

		BotCommand c = new UnwatchCommand(client, new BasicFormatter());
		assertEquals("The movie \"alien\" has been removed from the watched list.",
				c.response(createMockMessage(1, 1, "/unwatch alien")));

		assertEquals(0, UsersInfo.INSTANCE.getUserMovies(1).watched().size());
	}

	@Test
	public void testDescriptionAndHook() throws Exception {
		BotCommand c = new UnwatchCommand(client, new BasicFormatter());

		assertEquals("unwatch. Parameters: {FilmName}. Marks the given film as not watched.",
				c.hook() + ". " + c.description());
	}

	private Message createMockMessage(int userID, long chatID, String text) {
		String msg = UpdateBuilder.getMessage("pippo", userID, chatID, text, 1);
		Update update = BotUtils.parseUpdate(msg);
		return update.message();
	}
}
