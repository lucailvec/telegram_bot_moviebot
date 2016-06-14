package it.unimi.di.sweng.moviebot.commands;

import com.omertron.themoviedbapi.model.movie.MovieInfo;

import it.unimi.di.sweng.moviebot.client.MovieClient;
import it.unimi.di.sweng.moviebot.formatter.MovieFormatter;
import it.unimi.di.sweng.moviebot.model.MovieReferences;
import it.unimi.di.sweng.moviebot.model.UsersInfo;

public class IgnoreCommand extends MovieSearchCommand {

	public IgnoreCommand(MovieClient client, MovieFormatter formatter) {
		super(client, formatter);
	}

	@Override
	public String description() {
		return "Parameters: {FilmName}. Blacklists the given film so that it will not appear during searches.";
	}

	@Override
	public String hook() {
		return "ignore";
	}

	@Override
	public String response(int userID, MovieInfo movie) {
		UsersInfo.INSTANCE.blacklist(userID, MovieReferences.getMovieReference(movie.getId(), movie.getTitle()));
		return formatter.ignoreMovieReplyString(movie);
	}
}
