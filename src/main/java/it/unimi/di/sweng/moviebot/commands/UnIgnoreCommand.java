package it.unimi.di.sweng.moviebot.commands;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.model.movie.MovieInfo;

import it.unimi.di.sweng.moviebot.client.MovieClient;
import it.unimi.di.sweng.moviebot.formatter.MovieFormatter;
import it.unimi.di.sweng.moviebot.model.MovieReferences;
import it.unimi.di.sweng.moviebot.model.UsersInfo;

public class UnIgnoreCommand extends MovieSearchCommand {

	public UnIgnoreCommand(MovieClient client, MovieFormatter formatter) {
		super(client, formatter);
	}

	@Override
	public String description() {
		return "Parameters {FilmName}. removes the given film from the blacklist.";
	}

	@Override
	public String hook() {
		return "unignore";
	}

	@Override
	public String response(int userID, MovieInfo movie) throws MovieDbException {
		UsersInfo.INSTANCE.unblacklist(userID, MovieReferences.getMovieReference(movie.getId(), movie.getTitle()));
		return formatter.unignoreMovieReplyString(movie);
	}

}
