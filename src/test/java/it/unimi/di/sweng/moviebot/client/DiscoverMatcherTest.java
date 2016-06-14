package it.unimi.di.sweng.moviebot.client;

import static org.junit.Assert.*;

import org.junit.Test;

import com.omertron.themoviedbapi.model.discover.Discover;

public class DiscoverMatcherTest {
	@Test
	public void testMatcher() throws Exception {
		Discover discover = new Discover().page(1).language("en");
		Discover discover2 = new Discover().page(1).language("en");
		Discover discover3 = new Discover().page(2).language("en");

		DiscoverMatcher matcher = new DiscoverMatcher(discover);

		assertTrue(matcher.matches(discover2));
		assertFalse(matcher.matches(discover3));
	}
}
