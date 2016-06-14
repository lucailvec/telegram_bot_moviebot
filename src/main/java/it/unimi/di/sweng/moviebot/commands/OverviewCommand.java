package it.unimi.di.sweng.moviebot.commands;

import com.omertron.themoviedbapi.model.movie.MovieInfo;

import it.unimi.di.sweng.moviebot.client.MovieClient;
import it.unimi.di.sweng.moviebot.formatter.MovieFormatter;

public class OverviewCommand extends MovieSearchCommand {

	public OverviewCommand(MovieClient client, MovieFormatter formatter) {
		super(client, formatter);
	}

	@Override
	public String description() {
		return "Parameters: {FilmName}. Shows a brief synopsis about the given film.";
	}

	@Override
	public String hook() {
		return "overview";
	}

	@Override
	public String response(int userID, MovieInfo movie) {
		return formatter.overviewReplyString(movie);
	}
}
