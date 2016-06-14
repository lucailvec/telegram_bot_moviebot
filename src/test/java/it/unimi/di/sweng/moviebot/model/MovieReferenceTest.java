package it.unimi.di.sweng.moviebot.model;

import static org.junit.Assert.*;

import org.junit.Test;

import it.unimi.di.sweng.moviebot.model.MovieReferences.MovieReference;

public class MovieReferenceTest {

	@Test
	public void testEquals() {
		MovieReference m1 = MovieReferences.getMovieReference(1, "mymovie");
		MovieReference m2 = MovieReferences.getMovieReference(1, "mymovie");
		MovieReference m3 = MovieReferences.getMovieReference(2, "mymovie");

		assertEquals(m1, m2);
		assertNotEquals(m1, m3);
		
		assertNotEquals(m1, 1);
	}

	@Test
	public void testHashCode() {
		MovieReference m1 = MovieReferences.getMovieReference(1, "mymovie");
		MovieReference m2 = MovieReferences.getMovieReference(1, "mymovie");

		assertEquals(m1.hashCode(), m2.hashCode());
	}

	@Test
	public void testFlyweight() {
		MovieReference m1 = MovieReferences.getMovieReference(1, "mymovie");
		MovieReference m2 = MovieReferences.getMovieReference(1, "mymovie");

		assertTrue(m1 == m2);
	}
}
