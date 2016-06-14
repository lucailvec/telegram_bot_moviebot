package it.unimi.di.sweng.moviebot.commands;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.model.movie.MovieInfo;

import it.unimi.di.sweng.moviebot.client.MovieClient;
import it.unimi.di.sweng.moviebot.formatter.MovieFormatter;
import it.unimi.di.sweng.moviebot.model.MovieReferences;
import it.unimi.di.sweng.moviebot.model.UsersInfo;

public class UnwatchCommand extends MovieSearchCommand {

	public UnwatchCommand(MovieClient client, MovieFormatter formatter) {
		super(client, formatter);
	}

	@Override
	public String description() {
		return "Parameters: {FilmName}. Marks the given film as not watched.";
	}

	@Override
	public String hook() {
		return "unwatch";
	}

	@Override
	public String response(int userID, MovieInfo movie) throws MovieDbException {
		UsersInfo.INSTANCE.unwatch(userID, MovieReferences.getMovieReference(movie.getId(), movie.getTitle()));
		return formatter.unwatchMovieReplyString(movie);
	}

}
