package it.unimi.di.sweng.moviebot.states;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.omertron.themoviedbapi.MovieDbException;
import com.pengrad.telegrambot.model.Message;

import it.unimi.di.sweng.moviebot.client.MovieClient;
import it.unimi.di.sweng.moviebot.client.MovieClientFactory;
import it.unimi.di.sweng.moviebot.commands.BotCommand;
import it.unimi.di.sweng.moviebot.commands.GenreCommand;
import it.unimi.di.sweng.moviebot.commands.HelpCommand;
import it.unimi.di.sweng.moviebot.commands.IgnoreCommand;
import it.unimi.di.sweng.moviebot.commands.IgnoredListCommand;
import it.unimi.di.sweng.moviebot.commands.InfoCommand;
import it.unimi.di.sweng.moviebot.commands.NotToWatchCommand;
import it.unimi.di.sweng.moviebot.commands.OverviewCommand;
import it.unimi.di.sweng.moviebot.commands.PopularCommand;
import it.unimi.di.sweng.moviebot.commands.RecentCommand;
import it.unimi.di.sweng.moviebot.commands.StartCommand;
import it.unimi.di.sweng.moviebot.commands.ToWatchCommand;
import it.unimi.di.sweng.moviebot.commands.ToWatchListCommand;
import it.unimi.di.sweng.moviebot.commands.TrailerCommand;
import it.unimi.di.sweng.moviebot.commands.UnIgnoreCommand;
import it.unimi.di.sweng.moviebot.commands.UnwatchCommand;
import it.unimi.di.sweng.moviebot.commands.WatchCommand;
import it.unimi.di.sweng.moviebot.commands.WatchedListCommand;
import it.unimi.di.sweng.moviebot.formatter.BasicFormatter;
import it.unimi.di.sweng.moviebot.formatter.MovieFormatter;

public class InitialState implements BotState {
	private static final String MESSAGE_COMMAND_NOT_FOUND = "Command not found. Type /help for a list of commands.";
	private static String TELEGRAM_COMMAND_PREFIX = "/";
	
	private final Map<String, BotCommand> commands;

	public InitialState(MovieClientFactory factory) {
		MovieFormatter formatter = new BasicFormatter();
		MovieClient client = factory.createClient();

		commands = new HashMap<String, BotCommand>();

		addCommand(new GenreCommand(client, formatter));
		addCommand(new HelpCommand(commands));
		addCommand(new IgnoreCommand(client, formatter));
		addCommand(new IgnoredListCommand(client, formatter));
		addCommand(new InfoCommand());
		addCommand(new NotToWatchCommand(client, formatter));
		addCommand(new OverviewCommand(client, formatter));
		addCommand(new PopularCommand(client, formatter));
		addCommand(new RecentCommand(client, formatter));
		addCommand(new StartCommand());
		addCommand(new ToWatchCommand(client, formatter));
		addCommand(new TrailerCommand(client, formatter));
		addCommand(new UnIgnoreCommand(client, formatter));
		addCommand(new UnwatchCommand(client, formatter));
		addCommand(new WatchCommand(client, formatter));
		addCommand(new WatchedListCommand(client, formatter));
		addCommand(new ToWatchListCommand(client, formatter));
	}

	private void addCommand(BotCommand command) {
		commands.put(TELEGRAM_COMMAND_PREFIX + command.hook(), command);
	}

	@Override
	public String handle(Logger logger, Message message) {
		int userID = message.from().id();
		String text = message.text();

		String reply;

		final String[] tokens = text.split(" ");
		if (commands.containsKey(tokens[0]))
			try {
				reply = commands.get(tokens[0]).response(message);
				logger.info("Command executed for user " + userID + ": " + text);
			} catch (MovieDbException ex) {
				reply = "There was an error with TheMovieDB, please try again later.\n" + ex.getMessage();
				logger.warning("MovieDB error: " + ex.toString());
			} catch (IllegalArgumentException ex) {
				reply = ex.getMessage();
				logger.info("Exception due to invalid parameter from user " + userID + ": " + ex.getMessage());
			}
		else {
			reply = MESSAGE_COMMAND_NOT_FOUND;
			logger.info("User " + userID + " tried to enter invalid command: " + text);
		}

		return reply;
	}
}