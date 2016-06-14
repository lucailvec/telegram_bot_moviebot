package it.unimi.di.sweng.moviebot.commands;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import com.omertron.themoviedbapi.MovieDbException;
import com.pengrad.telegrambot.model.Message;

import it.unimi.di.sweng.moviebot.client.MovieClient;
import it.unimi.di.sweng.moviebot.formatter.MovieFormatter;
import it.unimi.di.sweng.moviebot.model.MovieReferences.MovieReference;
import it.unimi.di.sweng.moviebot.model.UserMovies;
import it.unimi.di.sweng.moviebot.model.UsersInfo;

public class RecentCommand extends MovieCommand {

	public RecentCommand(MovieClient client, MovieFormatter formatter) {
		super(client, formatter);
	}

	private final int DEFAULT_SIZE = 10;
	private final int MAX_SIZE = 25;
	private final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	@Override
	public String response(Message message) throws MovieDbException {
		String[] args = message.text().split(" ");
		int size = DEFAULT_SIZE;

		if (args.length < 2 || args.length > 3)
			throw new IllegalArgumentException("Invalid parameters");

		if (args.length == 3)
			try {
				size = Integer.parseInt(args[2]);
			}
			catch (NumberFormatException ex) {
				throw new IllegalArgumentException("Movies number must be a number.");
			}

		LocalDate date;
		try {
			date = LocalDate.parse(args[1], DEFAULT_FORMATTER);
		}
		catch (DateTimeParseException ex) {
			throw new IllegalArgumentException("Invalid date. Please format it as dd-mm-yyyy.");
		}

		UserMovies ui = UsersInfo.INSTANCE.getUserMovies(message.from().id());
		List<MovieReference> excluded = ui.watched();
		excluded.addAll(ui.blacklisted());
		excluded.addAll(ui.watchlist());

		return formatter.recentReplyString(client.retrievePopularsAfter(date, Math.min(size, MAX_SIZE), excluded));
	}

	@Override
	public String description() {
		return "Paramteres: {ReleaseDate} [n]. Suggests n (default: 10. max: 25) films that were released in or after the given date for the user to watch.";
	}

	@Override
	public String hook() {
		return "recent";
	}

}
