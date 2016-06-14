package it.unimi.di.sweng.moviebot.commands;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;

import it.unimi.di.sweng.moviebot.commands.BotCommand;
import it.unimi.di.sweng.moviebot.commands.HelpCommand;
import it.unimi.di.sweng.moviebot.model.MovieReferences;
import it.unimi.di.sweng.moviebot.model.UsersInfo;
import it.unimi.di.sweng.moviebot.server.UpdateBuilder;

public class HelpCommandTest {

	@Before
	public void set() {
		UsersInfo.INSTANCE.clearInfo();
		MovieReferences.clearAllReferences();
	}

	@Test
	public void testMan() throws Exception {
		Map<String, BotCommand> commands = new HashMap<String, BotCommand>();

		BotCommand c = new HelpCommand(commands);

		commands.put(c.hook(), c);
		assertEquals("/help: Shows a list of available commands.\n", c.response(createMockMessage(1, 1, "/help")));
	}

	private Message createMockMessage(int userID, long chatID, String text) {
		String msg = UpdateBuilder.getMessage("pippo", userID, chatID, text, 1);
		Update update = BotUtils.parseUpdate(msg);
		return update.message();
	}
}
