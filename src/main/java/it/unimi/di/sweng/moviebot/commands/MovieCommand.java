package it.unimi.di.sweng.moviebot.commands;

import it.unimi.di.sweng.moviebot.client.MovieClient;
import it.unimi.di.sweng.moviebot.formatter.MovieFormatter;

public abstract class MovieCommand implements BotCommand {
	protected final MovieClient client;
	protected final MovieFormatter formatter;

	public MovieCommand(MovieClient client, MovieFormatter formatter) {
		this.client = client;
		this.formatter = formatter;
	}
}
