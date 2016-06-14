package it.unimi.di.sweng.moviebot.commands;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.model.movie.MovieInfo;

import it.unimi.di.sweng.moviebot.client.MovieClient;
import it.unimi.di.sweng.moviebot.formatter.MovieFormatter;

public class TrailerCommand extends MovieSearchCommand {

	public TrailerCommand(MovieClient client, MovieFormatter formatter) {
		super(client, formatter);
	}

	@Override
	public String description() {
		return "Parameters: {FilmName}. Shows trailer links for the given film.";
	}

	@Override
	public String hook() {
		return "trailer";
	}

	@Override
	public String response(int userID, MovieInfo movie) throws MovieDbException {
		return formatter.trailerReplyString(client.retrieveMovieVideos(movie.getId()));
	}

}
