package it.unimi.di.sweng.moviebot.commands;

import java.util.List;

import com.omertron.themoviedbapi.MovieDbException;
import com.pengrad.telegrambot.model.Message;

import it.unimi.di.sweng.moviebot.client.MovieClient;
import it.unimi.di.sweng.moviebot.formatter.MovieFormatter;
import it.unimi.di.sweng.moviebot.model.MovieReferences.MovieReference;
import it.unimi.di.sweng.moviebot.model.UserMovies;
import it.unimi.di.sweng.moviebot.model.UsersInfo;

public class PopularCommand extends MovieCommand {

	public PopularCommand(MovieClient client, MovieFormatter formatter) {
		super(client, formatter);
	}

	private final int DEFAULT_SIZE = 10;
	private final int MAX_SIZE = 25;

	@Override
	public String response(Message message) throws MovieDbException {
		int size = DEFAULT_SIZE;
		String[] args = message.text().split(" ");

		if (args.length == 2)
			try {
				size = Integer.parseInt(args[1]);
			}
			catch (NumberFormatException ex) {
				throw new IllegalArgumentException("Movies number must be a number.");
			}
		else if (args.length > 2)
			throw new IllegalArgumentException("Invalid parameters");

		UserMovies ui = UsersInfo.INSTANCE.getUserMovies(message.from().id());
		List<MovieReference> excluded = ui.watched();
		excluded.addAll(ui.blacklisted());
		excluded.addAll(ui.watchlist());

		return formatter.popularReplyString(client.retrievePopulars(Math.min(size, MAX_SIZE), excluded));

	}

	@Override
	public String description() {
		return "Parameters: [n]. Suggests n (default: 10. max: 25) films for the user to watch.";
	}

	@Override
	public String hook() {
		return "popular";
	}
}
