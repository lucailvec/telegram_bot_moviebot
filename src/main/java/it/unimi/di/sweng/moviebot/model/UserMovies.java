package it.unimi.di.sweng.moviebot.model;

import java.util.ArrayList;
import java.util.List;

import it.unimi.di.sweng.moviebot.model.MovieReferences.MovieReference;

public class UserMovies {
	private final List<MovieReference> watched;
	private final List<MovieReference> blacklist;
	private final List<MovieReference> watchlist;

	public List<MovieReference> watched() {
		return new ArrayList<MovieReference>(watched);
	}

	public List<MovieReference> blacklisted() {
		return new ArrayList<MovieReference>(blacklist);
	}

	public List<MovieReference> watchlist() {
		return new ArrayList<MovieReference>(watchlist);
	}

	public UserMovies() {
		this.watched = new ArrayList<MovieReference>();
		this.blacklist = new ArrayList<MovieReference>();
		this.watchlist = new ArrayList<MovieReference>();
	}

	private void remove(List<MovieReference> list, MovieReference movie, String errorMessage) {
		if (!list.contains(movie))
			throw new IllegalArgumentException(errorMessage);

		list.remove(movie);
	}

	protected void unwatch(MovieReference movie) {
		remove(watched, movie, "Movie has not been watched yet.");
	}

	protected void unblacklist(MovieReference movie) {
		remove(blacklist, movie, "Movie was not on the blacklist.");
	}

	protected void removeFromWatchlist(MovieReference movie) {
		remove(watchlist, movie, "Movie was not on the watchlist.");
	}

	protected void watch(MovieReference movie) {
		checkPresent(movie);

		if (watchlist.contains(movie))
			watchlist.remove(movie);

		watched.add(movie);
	}

	protected void blacklist(MovieReference movie) {
		checkPresent(movie);

		if (watchlist.contains(movie))
			watchlist.remove(movie);

		blacklist.add(movie);
	}

	protected void addToWatchlist(MovieReference movie) {
		checkPresent(movie);

		if (watchlist.contains(movie))
			throw new IllegalArgumentException("Movie is already on the watchlist.");

		watchlist.add(movie);
	}

	private void checkPresent(MovieReference movie) {
		if (watched.contains(movie))
			throw new IllegalArgumentException("Movie has been watched.");

		if (blacklist.contains(movie))
			throw new IllegalArgumentException("Movie has been blacklisted.");
	}
}
