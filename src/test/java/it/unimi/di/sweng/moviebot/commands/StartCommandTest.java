package it.unimi.di.sweng.moviebot.commands;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;

import it.unimi.di.sweng.moviebot.server.UpdateBuilder;

public class StartCommandTest {

	private BotCommand command = new StartCommand();

	@Test
	public void testCommand() throws Exception {
		assertEquals("Welcome to MovieBot! This bot provides support to discover new movies using TheMovieDB.",
				command.response(createMockMessage(1, 1, "/start")));

		assertEquals("start. Displays a welcome message.", command.hook() + ". " + command.description());
	}

	private Message createMockMessage(int userID, long chatID, String text) {
		String msg = UpdateBuilder.getMessage("pippo", userID, chatID, text, 1);
		Update update = BotUtils.parseUpdate(msg);
		return update.message();
	}
}
