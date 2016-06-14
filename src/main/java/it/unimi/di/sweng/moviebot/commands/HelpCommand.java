package it.unimi.di.sweng.moviebot.commands;

import java.util.Map;

import com.pengrad.telegrambot.model.Message;

public class HelpCommand implements BotCommand {

	private Map<String, BotCommand> commands;
	private static final String TELEGRAM_COMMAND_PREFIX = "/";
	
	public HelpCommand(Map<String, BotCommand> commands) {
		this.commands = commands;
	}

	@Override
	public String response(Message message) {
		String s = "";
		for (BotCommand command : commands.values())
			s += TELEGRAM_COMMAND_PREFIX + command.hook() + ": " + command.description() + "\n";
		return s;
	}

	@Override
	public String description() {
		return "Shows a list of available commands.";
	}

	@Override
	public String hook() {
		return "help";
	}

}
