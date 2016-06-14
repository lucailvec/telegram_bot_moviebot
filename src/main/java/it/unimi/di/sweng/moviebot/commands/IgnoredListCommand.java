package it.unimi.di.sweng.moviebot.commands;

import com.omertron.themoviedbapi.MovieDbException;
import com.pengrad.telegrambot.model.Message;

import it.unimi.di.sweng.moviebot.client.MovieClient;
import it.unimi.di.sweng.moviebot.formatter.MovieFormatter;
import it.unimi.di.sweng.moviebot.model.UsersInfo;

public class IgnoredListCommand extends MovieCommand {
	public IgnoredListCommand(MovieClient client, MovieFormatter formatter) {
		super(client, formatter);
	}

	@Override
	public String response(Message message) throws MovieDbException {
		return formatter.ignoredListReplyString(UsersInfo.INSTANCE.getUserMovies(message.from().id()).blacklisted());
	}

	@Override
	public String description() {
		return "Shows all the ignored films.";
	}

	@Override
	public String hook() {
		return "ignoredlist";
	}

}
