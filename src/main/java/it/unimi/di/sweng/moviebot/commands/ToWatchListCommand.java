package it.unimi.di.sweng.moviebot.commands;

import com.omertron.themoviedbapi.MovieDbException;
import com.pengrad.telegrambot.model.Message;

import it.unimi.di.sweng.moviebot.client.MovieClient;
import it.unimi.di.sweng.moviebot.formatter.MovieFormatter;
import it.unimi.di.sweng.moviebot.model.UsersInfo;

public class ToWatchListCommand extends MovieCommand {

	public ToWatchListCommand(MovieClient client, MovieFormatter formatter) {
		super(client, formatter);
	}

	@Override
	public String response(Message message) throws MovieDbException {
		return formatter.toWatchListReplyString(UsersInfo.INSTANCE.getUserMovies(message.from().id()).watchlist());
	}

	@Override
	public String description() {
		return "Shows all films currently on the watchlist.";
	}

	@Override
	public String hook() {
		return "towatchlist";
	}
}
