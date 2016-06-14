package it.unimi.di.sweng.moviebot.commands;

import java.util.List;

import com.omertron.themoviedbapi.MovieDbException;
import com.pengrad.telegrambot.model.Message;

import it.unimi.di.sweng.moviebot.client.MovieClient;
import it.unimi.di.sweng.moviebot.formatter.MovieFormatter;
import it.unimi.di.sweng.moviebot.model.MovieReferences.MovieReference;
import it.unimi.di.sweng.moviebot.model.UserMovies;
import it.unimi.di.sweng.moviebot.model.UsersInfo;

public class GenreCommand extends MovieCommand {

	private final int DEFAULT_SIZE = 10;
	private final int MAX_SIZE = 25;

	public GenreCommand(MovieClient client, MovieFormatter formatter) {
		super(client, formatter);
	}

	@Override
	public String response(Message message) throws MovieDbException {
		String[] args = message.text().split(" ");
		int size = DEFAULT_SIZE;

		if (args.length < 2)
			throw new IllegalArgumentException("Invalid parameters");

		String genre = "";
		for (int i = 1; i < args.length - 1; i++)
			genre += args[i] + " ";

		try {
			size = Integer.parseInt(args[args.length - 1]);
		} catch (Exception e) {
			genre += args[args.length - 1];
		}

		UserMovies ui = UsersInfo.INSTANCE.getUserMovies(message.from().id());
		List<MovieReference> excluded = ui.watched();
		excluded.addAll(ui.blacklisted());
		excluded.addAll(ui.watchlist());

		return formatter.genreReplyString(client.retrievePopularsByGenre(genre.trim(), Math.min(size, MAX_SIZE), excluded));
	}

	@Override
	public String description() {
		return "Parameters: {FilmName} [n]. Suggests n (default: 10. max: 25) films of the given genre for the user to watch.";
	}

	@Override
	public String hook() {
		return "genre";
	}
}
