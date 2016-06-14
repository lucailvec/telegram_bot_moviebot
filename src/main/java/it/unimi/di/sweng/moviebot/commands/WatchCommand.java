package it.unimi.di.sweng.moviebot.commands;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.model.movie.MovieInfo;

import it.unimi.di.sweng.moviebot.client.MovieClient;
import it.unimi.di.sweng.moviebot.formatter.MovieFormatter;
import it.unimi.di.sweng.moviebot.model.MovieReferences;
import it.unimi.di.sweng.moviebot.model.UsersInfo;

public class WatchCommand extends MovieSearchCommand {

	public WatchCommand(MovieClient client, MovieFormatter formatter) {
		super(client, formatter);
	}

	@Override
	public String description() {
		return "Parameters: {FilmName}. Marks the given film as watched. If the film is currently on the watchlist, it is removed from the watchlist.";
	}

	@Override
	public String hook() {
		return "watch";
	}

	@Override
	public String response(int userID, MovieInfo movie) throws MovieDbException {
		UsersInfo.INSTANCE.watch(userID, MovieReferences.getMovieReference(movie.getId(), movie.getTitle()));
		return formatter.watchMovieReplyString(movie);
	}

}
