package it.unimi.di.sweng.moviebot.commands;

import com.omertron.themoviedbapi.MovieDbException;
import com.pengrad.telegrambot.model.Message;

import it.unimi.di.sweng.moviebot.client.MovieClient;
import it.unimi.di.sweng.moviebot.formatter.MovieFormatter;
import it.unimi.di.sweng.moviebot.model.UsersInfo;

public class WatchedListCommand extends MovieCommand {

	public WatchedListCommand(MovieClient client, MovieFormatter formatter) {
		super(client, formatter);
	}

	@Override
	public String response(Message message) throws MovieDbException {
		return formatter.watchedListReplyString(UsersInfo.INSTANCE.getUserMovies(message.from().id()).watched());
	}

	@Override
	public String description() {
		return "Shows all the viewed films.";
	}

	@Override
	public String hook() {
		return "watchedlist";
	}

}
