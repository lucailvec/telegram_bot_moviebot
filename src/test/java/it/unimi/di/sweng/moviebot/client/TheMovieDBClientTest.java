package it.unimi.di.sweng.moviebot.client;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.enumeration.SortBy;
import com.omertron.themoviedbapi.model.Genre;
import com.omertron.themoviedbapi.model.discover.Discover;
import com.omertron.themoviedbapi.model.media.Video;
import com.omertron.themoviedbapi.model.movie.MovieBasic;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.omertron.themoviedbapi.results.ResultList;

import it.unimi.di.sweng.moviebot.model.MovieReferences;
import it.unimi.di.sweng.moviebot.model.MovieReferences.MovieReference;

public class TheMovieDBClientTest {

	private TheMovieDbApi mockApi;
	private TheMovieDBClient client;

	@Before
	public void setUp() {
		mockApi = mock(TheMovieDbApi.class);
		client = new TheMovieDBClient(mockApi);
	}

	@Test
	public void testRetrieveMovieInfo() throws MovieDbException {
		MovieInfo movie = createMockMovie(1, "mymovie");

		when(mockApi.getMovieInfo(1, client.LOCALE)).thenReturn(movie);

		assertEquals(movie, client.retrieveMovie(1));
	}

	@Test
	public void testRetrieveMovieVideos() throws Exception {
		Video video = createMockVideo("123", "mysite");
		Video video2 = createMockVideo("456", "myothersite");

		ResultList<Video> videos = new ResultList<Video>();
		videos.setResults(Arrays.asList(video, video2));

		when(mockApi.getMovieVideos(1, client.LOCALE)).thenReturn(videos);

		assertEquals(Arrays.asList(video, video2), client.retrieveMovieVideos(1));
	}

	@Test
	public void testSearchMovieSinglePage() throws Exception {
		MovieInfo movie = createMockMovie(1, "mymovie");
		MovieInfo movie2 = createMockMovie(2, "mymovie2");

		ResultList<MovieInfo> movies = createMockMovieInfoResults(1, 1, movie, movie2);

		when(mockApi.searchMovie("mymovie", 1, client.LOCALE, null, null, null, null)).thenReturn(movies);

		assertEquals(Arrays.asList(movie, movie2), client.searchMovie("mymovie", 5));
		assertEquals(Arrays.asList(movie), client.searchMovie("mymovie", 1));
	}

	@Test
	public void testSearchMovieMultiplePages() throws Exception {
		MovieInfo movie = createMockMovie(1, "mymovie");
		MovieInfo movie2 = createMockMovie(2, "mymovie2");

		ResultList<MovieInfo> movies1 = createMockMovieInfoResults(1, 2, movie);
		ResultList<MovieInfo> movies2 = createMockMovieInfoResults(2, 2, movie2);

		when(mockApi.searchMovie("mymovie", 1, client.LOCALE, null, null, null, null)).thenReturn(movies1);
		when(mockApi.searchMovie("mymovie", 2, client.LOCALE, null, null, null, null)).thenReturn(movies2);

		assertEquals(Arrays.asList(movie, movie2), client.searchMovie("mymovie", 5));
		assertEquals(Arrays.asList(movie), client.searchMovie("mymovie", 1));
	}

	@Test
	public void testDiscoverMovies() throws Exception {
		MovieBasic movie = createMockMovie(1, "mymovie");
		MovieBasic movie2 = createMockMovie(2, "mymovie2");

		ResultList<MovieBasic> movies1 = createMockMovieBasicResults(1, 2, movie);
		ResultList<MovieBasic> movies2 = createMockMovieBasicResults(2, 2, movie2);

		DiscoverMatcher page1 = new DiscoverMatcher(
				new Discover().page(1).language(client.LOCALE).sortBy(SortBy.POPULARITY_DESC));
		DiscoverMatcher page2 = new DiscoverMatcher(
				new Discover().page(2).language(client.LOCALE).sortBy(SortBy.POPULARITY_DESC));

		when(mockApi.getDiscoverMovies(argThat(page1))).thenReturn(movies1);
		when(mockApi.getDiscoverMovies(argThat(page2))).thenReturn(movies2);

		assertEquals(Arrays.asList(movie, movie2), client.retrievePopulars(5, new ArrayList<MovieReference>()));
		assertEquals(Arrays.asList(movie), client.retrievePopulars(1, new ArrayList<MovieReference>()));
	}

	@Test
	public void testDiscoverMoviesWithFilter() throws Exception {
		MovieBasic movie = createMockMovie(1, "mymovie");
		MovieBasic movie2 = createMockMovie(2, "mymovie2");

		ResultList<MovieBasic> movies = createMockMovieBasicResults(1, 1, movie, movie2);

		DiscoverMatcher page = new DiscoverMatcher(
				new Discover().page(1).language(client.LOCALE).sortBy(SortBy.POPULARITY_DESC));

		when(mockApi.getDiscoverMovies(argThat(page))).thenReturn(movies);
		assertEquals(Arrays.asList(movie2),
				client.retrievePopulars(5, Arrays.asList(MovieReferences.getMovieReference(1, "mymovie"))));
	}

	@Test
	public void testDiscoverGenreMovies() throws Exception {
		MovieBasic movie = createMockMovie(1, "mymovie");
		movie.setGenreIds(Arrays.asList(1));
		MovieBasic movie2 = createMockMovie(2, "mymovie2");
		movie2.setGenreIds(Arrays.asList(2));

		Genre genre = createMockGenre(1, "mygenre");
		Genre genre2 = createMockGenre(2, "myothergenre");

		ResultList<Genre> genres = new ResultList<Genre>();
		genres.setResults(Arrays.asList(genre, genre2));

		ResultList<MovieBasic> movies = createMockMovieBasicResults(1, 1, movie);

		DiscoverMatcher page = new DiscoverMatcher(
				new Discover().page(1).language(client.LOCALE).sortBy(SortBy.POPULARITY_DESC).withGenres("1"));

		when(mockApi.getGenreMovieList(client.LOCALE)).thenReturn(genres);
		when(mockApi.getDiscoverMovies(argThat(page))).thenReturn(movies);

		assertEquals(Arrays.asList(movie),
				client.retrievePopularsByGenre("mygenre", 5, new ArrayList<MovieReference>()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGenreNotFound() throws Exception {
		Genre genre = createMockGenre(1, "mygenre");
		Genre genre2 = createMockGenre(2, "myothergenre");

		ResultList<Genre> genres = new ResultList<Genre>();
		genres.setResults(Arrays.asList(genre, genre2));

		when(mockApi.getGenreMovieList(client.LOCALE)).thenReturn(genres);

		client.retrievePopularsByGenre("BAD", 5, new ArrayList<MovieReference>());
	}

	@Test
	public void testDiscoverMoviesAfter() throws Exception {
		MovieBasic movie = createMockMovie(1, "mymovie");
		movie.setReleaseDate("2010-05-15");
		MovieBasic movie2 = createMockMovie(2, "mymovie2");
		movie.setReleaseDate("2010-12-15");

		LocalDate date = LocalDate.parse("01-08-2015", DateTimeFormatter.ofPattern("dd-MM-yyyy"));

		ResultList<MovieBasic> movies = createMockMovieBasicResults(1, 1, movie2);

		DiscoverMatcher page = new DiscoverMatcher(new Discover().page(1).language(client.LOCALE)
				.sortBy(SortBy.POPULARITY_DESC).releaseDateGte("2015-08-01"));

		when(mockApi.getDiscoverMovies(argThat(page))).thenReturn(movies);
		assertEquals(Arrays.asList(movie2),
				client.retrievePopularsAfter(date, 5, Arrays.asList(MovieReferences.getMovieReference(1, "mymovie"))));
	}

	private MovieInfo createMockMovie(int id, String title) {
		MovieInfo movie = new MovieInfo();
		movie.setId(id);
		movie.setTitle(title);
		return movie;
	}

	private Video createMockVideo(String id, String site) {
		Video video = new Video();
		video.setSite(site);
		video.setId(id);
		return video;
	}

	private Genre createMockGenre(int id, String name) {
		Genre genre = new Genre();
		genre.setId(id);
		genre.setName(name);
		return genre;
	}

	private ResultList<MovieInfo> createMockMovieInfoResults(int page, int totalPages, MovieInfo... results) {
		ResultList<MovieInfo> movies = new ResultList<MovieInfo>();
		movies.setPage(page);
		movies.setTotalPages(totalPages);
		movies.setResults(Arrays.asList(results));
		return movies;
	}

	private ResultList<MovieBasic> createMockMovieBasicResults(int page, int totalPages, MovieBasic... results) {
		ResultList<MovieBasic> movies = new ResultList<MovieBasic>();
		movies.setPage(page);
		movies.setTotalPages(totalPages);
		movies.setResults(Arrays.asList(results));
		return movies;
	}
}
