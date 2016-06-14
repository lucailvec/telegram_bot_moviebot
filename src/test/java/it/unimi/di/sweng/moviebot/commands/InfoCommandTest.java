package it.unimi.di.sweng.moviebot.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;

import it.unimi.di.sweng.moviebot.model.MovieReferences;
import it.unimi.di.sweng.moviebot.model.UsersInfo;
import it.unimi.di.sweng.moviebot.server.UpdateBuilder;

public class InfoCommandTest {

	@Before
	public void set() {
		UsersInfo.INSTANCE.clearInfo();
		MovieReferences.clearAllReferences();
	}

	@Test
	public void testMan() throws Exception {
		BotCommand c = new InfoCommand();
		assertEquals(
				"This bot was developed as software engeneering course project by unimi students.\n"
						+ "It uses TheMovieDB to retrive informations (www.themoviedb.org).",
				c.response(createMockMessage(1, 1, "/info")));
	}

	@Test
	public void testDescriptionAndHook() throws Exception {
		BotCommand c = new InfoCommand();

		assertEquals("info. shows information about this bot.", c.hook() + ". " + c.description());
	}

	private Message createMockMessage(int userID, long chatID, String text) {
		String msg = UpdateBuilder.getMessage("pippo", userID, chatID, text, 1);
		Update update = BotUtils.parseUpdate(msg);
		return update.message();
	}
}