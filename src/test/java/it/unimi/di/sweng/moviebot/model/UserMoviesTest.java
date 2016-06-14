package it.unimi.di.sweng.moviebot.model;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import it.unimi.di.sweng.moviebot.model.MovieReferences.MovieReference;

public class UserMoviesTest {

	private UserMovies userMovies;

	@Before
	public void setUp() {
		userMovies = new UserMovies();
	}

	@Test
	public void testWatched() {
		MovieReference movie = MovieReferences.getMovieReference(1, "mymovie");

		userMovies.watch(movie);
		assertEquals(Arrays.asList(movie), userMovies.watched());

		userMovies.unwatch(movie);
		assertEquals(Arrays.asList(), userMovies.watched());
	}

	@Test
	public void testBlacklist() {
		MovieReference movie = MovieReferences.getMovieReference(1, "mymovie");

		userMovies.blacklist(movie);
		assertEquals(Arrays.asList(movie), userMovies.blacklisted());

		userMovies.unblacklist(movie);
		assertEquals(Arrays.asList(), userMovies.blacklisted());
	}

	@Test
	public void testWatchlist() {
		MovieReference movie = MovieReferences.getMovieReference(1, "mymovie");

		userMovies.addToWatchlist(movie);
		assertEquals(Arrays.asList(movie), userMovies.watchlist());

		userMovies.removeFromWatchlist(movie);
		assertEquals(Arrays.asList(), userMovies.watchlist());
	}

	@Test
	public void testWatchFromWatchlist() {
		MovieReference movie = MovieReferences.getMovieReference(1, "mymovie");

		userMovies.addToWatchlist(movie);
		assertEquals(Arrays.asList(movie), userMovies.watchlist());

		userMovies.watch(movie);
		assertEquals(Arrays.asList(), userMovies.watchlist());
	}

	@Test
	public void testBlacklistFromWatchlist() {
		MovieReference movie = MovieReferences.getMovieReference(1, "mymovie");

		userMovies.addToWatchlist(movie);
		assertEquals(Arrays.asList(movie), userMovies.watchlist());

		userMovies.blacklist(movie);
		assertEquals(Arrays.asList(), userMovies.watchlist());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAlreadyWatched() {
		MovieReference movie = MovieReferences.getMovieReference(1, "mymovie");

		userMovies.watch(movie);
		userMovies.watch(movie);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAlreadyBlacklisted() {
		MovieReference movie = MovieReferences.getMovieReference(1, "mymovie");

		userMovies.blacklist(movie);
		userMovies.blacklist(movie);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAlreadyOnWatchlist() {
		MovieReference movie = MovieReferences.getMovieReference(1, "mymovie");

		userMovies.addToWatchlist(movie);
		userMovies.addToWatchlist(movie);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWatchBlacklisted() {
		MovieReference movie = MovieReferences.getMovieReference(1, "mymovie");

		userMovies.blacklist(movie);
		userMovies.watch(movie);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBlacklistWatched() {
		MovieReference movie = MovieReferences.getMovieReference(1, "mymovie");

		userMovies.watch(movie);
		userMovies.blacklist(movie);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWatchlistWatched() {
		MovieReference movie = MovieReferences.getMovieReference(1, "mymovie");

		userMovies.watch(movie);
		userMovies.addToWatchlist(movie);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWatchlistBlacklisted() {
		MovieReference movie = MovieReferences.getMovieReference(1, "mymovie");

		userMovies.blacklist(movie);
		userMovies.addToWatchlist(movie);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnwatchNotWatched() {
		MovieReference movie = MovieReferences.getMovieReference(1, "mymovie");

		userMovies.unwatch(movie);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnblacklistNotBlacklisted() {
		MovieReference movie = MovieReferences.getMovieReference(1, "mymovie");

		userMovies.unblacklist(movie);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnwatchlistNotWatchlisted() {
		MovieReference movie = MovieReferences.getMovieReference(1, "mymovie");

		userMovies.removeFromWatchlist(movie);
	}
}
