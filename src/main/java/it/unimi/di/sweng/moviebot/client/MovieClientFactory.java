package it.unimi.di.sweng.moviebot.client;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import java.util.logging.Logger;

import it.unimi.di.sweng.moviebot.server.Configs;

public class MovieClientFactory {
	public MovieClientFactory() {
	}

	public MovieClientFactory(TheMovieDbApi mockApi) {
		this.mockApi = mockApi;
	}

	private TheMovieDbApi mockApi;

	public MovieClient createClient() {
		if (Configs.INSTANCE.TESTING_PHASE)
			return new TheMovieDBClient(mockApi);

		try {
			return new TheMovieDBClient(Configs.INSTANCE.MOVIEDB_API_KEY);
		} catch (MovieDbException impossible) {
			// looking in the API code, this exception is never thrown
			// even though there is a throws declaration
			Logger.getLogger(this.getClass().getName())
					.severe("Exception while creating moviedb client! It should never have happened.");

			throw new IllegalStateException("This exception should have never happened.");
		}
	}
}
