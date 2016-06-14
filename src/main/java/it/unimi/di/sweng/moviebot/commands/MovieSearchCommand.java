package it.unimi.di.sweng.moviebot.commands;

import java.util.Arrays;
import java.util.List;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.pengrad.telegrambot.model.Message;

import it.unimi.di.sweng.moviebot.client.MovieClient;
import it.unimi.di.sweng.moviebot.commands.MovieCommand;
import it.unimi.di.sweng.moviebot.formatter.MovieFormatter;
import it.unimi.di.sweng.moviebot.model.UsersInfo;
import it.unimi.di.sweng.moviebot.states.SelectionState;

public abstract class MovieSearchCommand extends MovieCommand {
	private final int MAX_SEARCH_OPTIONS = 10;
	
	public MovieSearchCommand(MovieClient client, MovieFormatter formatter) {
		super(client, formatter);
	}

	public abstract String response(int userID, MovieInfo movie) throws MovieDbException;

	@Override
	public String response(Message message) throws MovieDbException {
		String[] args = message.text().split(" ");

		if (args.length < 2)
			throw new IllegalArgumentException("Missing parameter {FilmName}");
		
		String movieTitle = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
		List<MovieInfo> searchResults = client.searchMovie(movieTitle, MAX_SEARCH_OPTIONS);

		switch (searchResults.size()) {
			case 0:
				throw new IllegalArgumentException("Film not found");
				
			case 1:
				MovieInfo movie = searchResults.get(0);
				if (movieTitle.equalsIgnoreCase(movie.getTitle()))
					return response(message.from().id(), searchResults.get(0));
				else 
					throw new IllegalArgumentException("Film not found");
				
			default:
				UsersInfo.INSTANCE.setChatState(message.chat().id(), new SelectionState(searchResults, this));
				return askUserSelection(searchResults);
		}
	}

	private String askUserSelection(List<MovieInfo> searchResults) {
		StringBuilder reply = new StringBuilder("Search results:\n");
		for (int i = 0; i < searchResults.size(); i++) {
			MovieInfo movie = searchResults.get(i);
			System.out.println(movie);
			reply.append(i+1 + ") " + movie.getTitle() + " (" + movie.getReleaseDate() + ")\n");
		}
		return reply.toString();
	}

	@Override
	public abstract String hook();

	@Override
	public abstract String description();
}
