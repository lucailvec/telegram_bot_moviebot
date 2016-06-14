package it.unimi.di.sweng.moviebot.model;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.omertron.themoviedbapi.model.movie.MovieInfo;

import it.unimi.di.sweng.moviebot.states.SelectionState;

public class UsersInfoTest {
	@Before
	public void setUp() {
		UsersInfo.INSTANCE.clearInfo();
	}

	@Test
	public void testCreateNewSession() throws Exception {
		assertEquals("InitialState", UsersInfo.INSTANCE.getChatState(1).getClass().getSimpleName());
	}

	@Test
	public void testNoSessionOverwrite() throws Exception {
		UsersInfo.INSTANCE.setChatState(1, 
				new SelectionState(Arrays.asList(new MovieInfo(), new MovieInfo()), null));
		assertEquals("SelectionState", UsersInfo.INSTANCE.getChatState(1).getClass().getSimpleName());
	}
}
