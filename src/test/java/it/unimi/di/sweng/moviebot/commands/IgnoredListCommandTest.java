package it.unimi.di.sweng.moviebot.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.omertron.themoviedbapi.MovieDbException;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;

import it.unimi.di.sweng.moviebot.client.MovieClient;
import it.unimi.di.sweng.moviebot.formatter.BasicFormatter;
import it.unimi.di.sweng.moviebot.model.MovieReferences;
import it.unimi.di.sweng.moviebot.model.UsersInfo;
import it.unimi.di.sweng.moviebot.server.UpdateBuilder;

@RunWith(MockitoJUnitRunner.class)
public class IgnoredListCommandTest {

	@Mock
	private static MovieClient client;

	@Before
	public void set() {
		UsersInfo.INSTANCE.clearInfo();
		MovieReferences.clearAllReferences();
	}

	@Test
	public void testIgnoredListCommand() throws MovieDbException {
		UsersInfo.INSTANCE.blacklist(1, MovieReferences.getMovieReference(1, "alien"));

		BotCommand c = new IgnoredListCommand(client, new BasicFormatter());

		assertEquals("The movies in your blacklist are :\n- alien\n", c.response(createMockMessage(1, 1, "/ignoredList")));
	}

	@Test
	public void testDescriptionAndHook() throws Exception {
		BotCommand c = new IgnoredListCommand(client, new BasicFormatter());

		assertEquals("ignoredlist. Shows all the ignored films.", c.hook() + ". " + c.description());
	}

	private Message createMockMessage(int userID, long chatID, String text) {
		String msg = UpdateBuilder.getMessage("pippo", userID, chatID, text, 1);
		Update update = BotUtils.parseUpdate(msg);
		return update.message();
	}
}
