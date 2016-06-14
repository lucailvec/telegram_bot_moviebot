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
import com.omertron.themoviedbapi.model.media.Video;
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
public class TrailerCommandTest {

	@Mock
	private static MovieClient client;
	private List<MovieInfo> mf;

	@Before
	public void set() {
		UsersInfo.INSTANCE.clearInfo();
		MovieReferences.clearAllReferences();
	}

	@Test
	public void testTrailerCommand() throws MovieDbException {
		MovieInfo m = new MovieInfo();

		List<Video> listOfVideos = new ArrayList<>();
		Video video = new Video();
		video.setSite("youtube");
		video.setKey("a123");
		listOfVideos.add(video);

		m.setTitle("alien");
		m.setId(1);
		mf = new ArrayList<MovieInfo>();
		mf.add(m);
		when(client.searchMovie("alien", 10)).thenReturn(mf);
		when(client.retrieveMovieVideos(1)).thenReturn(listOfVideos);
		BotCommand c = new TrailerCommand(client, new BasicFormatter());
		assertEquals("The available trailers on YouTube are :\n- https://www.youtube.com/watch?v=a123\n",
				(c.response(createMockMessage(1, 1, "/trailer alien"))));
	}

	@Test
	public void testDescriptionAndHook() throws Exception {
		BotCommand c = new TrailerCommand(client, new BasicFormatter());

		assertEquals("trailer. Parameters: {FilmName}. Shows trailer links for the given film.",
				c.hook() + ". " + c.description());
	}

	private Message createMockMessage(int userID, long chatID, String text) {
		String msg = UpdateBuilder.getMessage("pippo", userID, chatID, text, 1);
		Update update = BotUtils.parseUpdate(msg);
		return update.message();
	}
}
