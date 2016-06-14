package it.unimi.di.sweng.moviebot.commands;

import com.omertron.themoviedbapi.model.movie.MovieInfo;

import it.unimi.di.sweng.moviebot.client.MovieClient;
import it.unimi.di.sweng.moviebot.formatter.MovieFormatter;
import it.unimi.di.sweng.moviebot.model.MovieReferences;
import it.unimi.di.sweng.moviebot.model.UsersInfo;

public class ToWatchCommand extends MovieSearchCommand {

	public ToWatchCommand(MovieClient client, MovieFormatter formatter) {
		super(client, formatter);
	}

	@Override
	public String description() {
		return "Parameters: {FilmName}. Adds the given film to the watchlist.";
	}

	@Override
	public String hook() {
		return "towatch";
	}

	@Override
	public String response(int userID, MovieInfo movie) {
		UsersInfo.INSTANCE.addToWatchlist(userID, MovieReferences.getMovieReference(movie.getId(), movie.getTitle()));
		return formatter.toWatchMovieReplyString(movie);
	}

}
