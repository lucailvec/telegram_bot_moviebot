package it.unimi.di.sweng.moviebot.states;

import static org.junit.Assert.*;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.yamj.api.common.exception.ApiExceptionType;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.enumeration.SortBy;
import com.omertron.themoviedbapi.model.discover.Discover;
import com.omertron.themoviedbapi.model.movie.MovieBasic;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.omertron.themoviedbapi.results.ResultList;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;

import it.unimi.di.sweng.moviebot.client.DiscoverMatcher;
import it.unimi.di.sweng.moviebot.client.MovieClientFactory;
import it.unimi.di.sweng.moviebot.server.UpdateBuilder;

public class InitialStateTest {

	private static BotState state;
	private static TheMovieDbApi mockApi = mock(TheMovieDbApi.class);

	@Before
	public void setUp() {
		state = new InitialState(new MovieClientFactory(mockApi));
	}

	@Test
	public void testExistingCommand() throws Exception {
		MovieBasic movie = createMockMovie(1, "mymovie");
		MovieBasic movie2 = createMockMovie(2, "mymovie2");
		ResultList<MovieBasic> movies = createMockMovieBasicResults(1, 1, movie, movie2);
		DiscoverMatcher page = new DiscoverMatcher(
				new Discover().page(1).language("en").sortBy(SortBy.POPULARITY_DESC));

		when(mockApi.getDiscoverMovies(argThat(page))).thenReturn(movies);

		assertEquals("The most popular movies at the moment are :\n- mymovie\n- mymovie2\n",
				state.handle(Logger.getLogger(Logger.GLOBAL_LOGGER_NAME), createMockMessage(1, 1, "/popular")));
	}

	@Test
	public void testExceptionDuringCommand() throws Exception {
		assertEquals("Missing parameter {FilmName}",
				state.handle(Logger.getLogger(Logger.GLOBAL_LOGGER_NAME), createMockMessage(1, 1, "/overview")));
	}

	@Test
	public void testInvalidCommand() throws Exception {
		assertEquals("Command not found. Type /help for a list of commands.",
				state.handle(Logger.getLogger(Logger.GLOBAL_LOGGER_NAME), createMockMessage(1, 1, "/BAD")));
	}

	@Test
	public void testMovieDBException() throws Exception {
		when(mockApi.searchMovie("alien", 1, "en", null, null, null, null))
				.thenThrow(new MovieDbException(ApiExceptionType.CONNECTION_ERROR, "myerror"));

		assertEquals("There was an error with TheMovieDB, please try again later.\n" +
					 "ExceptionType=CONNECTION_ERROR, ResponseCode=0, URL=",
				state.handle(Logger.getLogger(Logger.GLOBAL_LOGGER_NAME), createMockMessage(1, 1, "/overview alien")));
	}

	private MovieInfo createMockMovie(int id, String title) {
		MovieInfo movie = new MovieInfo();
		movie.setId(id);
		movie.setTitle(title);
		return movie;
	}

	private ResultList<MovieBasic> createMockMovieBasicResults(int page, int totalPages, MovieBasic... results) {
		ResultList<MovieBasic> movies = new ResultList<MovieBasic>();
		movies.setPage(page);
		movies.setTotalPages(totalPages);
		movies.setResults(Arrays.asList(results));
		return movies;
	}

	private Message createMockMessage(int userID, long chatID, String text) {
		String msg = UpdateBuilder.getMessage("pippo", userID, chatID, text, 1);
		Update update = BotUtils.parseUpdate(msg);
		return update.message();
	}
}
