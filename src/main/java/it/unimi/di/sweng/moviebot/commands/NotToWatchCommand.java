package it.unimi.di.sweng.moviebot.commands;

import com.omertron.themoviedbapi.model.movie.MovieInfo;

import it.unimi.di.sweng.moviebot.client.MovieClient;
import it.unimi.di.sweng.moviebot.formatter.MovieFormatter;
import it.unimi.di.sweng.moviebot.model.MovieReferences;
import it.unimi.di.sweng.moviebot.model.UsersInfo;

public class NotToWatchCommand extends MovieSearchCommand {

	public NotToWatchCommand(MovieClient client, MovieFormatter formatter) {
		super(client, formatter);
	}

	@Override
	public String description() {
		return "Parameters: {FilmName}. Marks the given film as not watched.";
	}

	@Override
	public String hook() {
		return "nottowatch";
	}

	@Override
	public String response(int userID, MovieInfo movie) {
		UsersInfo.INSTANCE.removeFromWatchlist(userID,
				MovieReferences.getMovieReference(movie.getId(), movie.getTitle()));
		return formatter.notToWatchMovieReplyString(movie);
	}

}
