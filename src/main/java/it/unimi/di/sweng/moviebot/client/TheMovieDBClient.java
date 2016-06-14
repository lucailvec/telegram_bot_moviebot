package it.unimi.di.sweng.moviebot.client;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.enumeration.SortBy;
import com.omertron.themoviedbapi.model.Genre;
import com.omertron.themoviedbapi.model.discover.Discover;
import com.omertron.themoviedbapi.model.media.Video;
import com.omertron.themoviedbapi.model.movie.MovieBasic;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.omertron.themoviedbapi.results.ResultList;

import it.unimi.di.sweng.moviebot.model.MovieReferences.MovieReference;

public class TheMovieDBClient implements MovieClient {

	public final String LOCALE = "en";
	private TheMovieDbApi apiClient;

	public TheMovieDBClient(String apikey) throws MovieDbException {
		this(new TheMovieDbApi(apikey));
	}

	public TheMovieDBClient(TheMovieDbApi apiClient) {
		this.apiClient = apiClient;
	}

	@Override
	public List<MovieInfo> searchMovie(String name, int resultCount) throws MovieDbException {
		List<MovieInfo> result = new ArrayList<MovieInfo>();

		int page = 0;
		ResultList<MovieInfo> resultPage;
		do {
			resultPage = apiClient.searchMovie(name, ++page, LOCALE, null, null, null, null);
			result.addAll(resultPage.getResults());
		} while (result.size() < resultCount && page < resultPage.getTotalPages());

		return result.subList(0, Math.min(result.size(), resultCount));
	}

	@Override
	public MovieInfo retrieveMovie(int id) throws MovieDbException {
		return apiClient.getMovieInfo(id, LOCALE);
	}

	@Override
	public List<Video> retrieveMovieVideos(int id) throws MovieDbException {
		return apiClient.getMovieVideos(id, LOCALE).getResults();
	}

	@Override
	public List<MovieBasic> retrievePopulars(int movieCount, List<MovieReference> excludedMovies)
			throws MovieDbException {

		Discover discover = new Discover().language(LOCALE).sortBy(SortBy.POPULARITY_DESC);

		return retrieveDiscoverMovies(movieCount, excludedMovies, discover);
	}

	@Override
	public List<MovieBasic> retrievePopularsByGenre(String genre, int movieCount, List<MovieReference> excludedMovies)
			throws MovieDbException {

		Discover discover = new Discover().language(LOCALE).sortBy(SortBy.POPULARITY_DESC)
				.withGenres("" + retrieveGenreId(genre));

		return retrieveDiscoverMovies(movieCount, excludedMovies, discover);
	}

	@Override
	public List<MovieBasic> retrievePopularsAfter(LocalDate date, int movieCount, List<MovieReference> excludedMovies)
			throws MovieDbException {

		Discover discover = new Discover().language(LOCALE).sortBy(SortBy.POPULARITY_DESC)
				.releaseDateGte(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

		return retrieveDiscoverMovies(movieCount, excludedMovies, discover);
	}

	private int retrieveGenreId(String genreName) throws MovieDbException {
		ResultList<Genre> genres = apiClient.getGenreMovieList(LOCALE);
		for (Genre g : genres.getResults())
			if (g.getName().equalsIgnoreCase(genreName))
				return g.getId();

		throw new IllegalArgumentException("Genre not found.");
	}

	private List<MovieBasic> retrieveDiscoverMovies(int movieCount, List<MovieReference> excludedMovies,
			Discover discover) throws MovieDbException {

		List<MovieBasic> result = new ArrayList<MovieBasic>();
		int page = 0;
		ResultList<MovieBasic> resultPage;

		do {
			resultPage = apiClient.getDiscoverMovies(discover.page(++page));

			for (MovieBasic movie : resultPage.getResults())
				if (!exclude(movie, excludedMovies))
					result.add(movie);

		} while (result.size() < movieCount && page < resultPage.getTotalPages());

		return result.subList(0, Math.min(result.size(), movieCount));
	}

	private boolean exclude(MovieBasic movie, List<MovieReference> excludedMovies) {
		for (MovieReference excludedMovie : excludedMovies)
			if (excludedMovie.ID == movie.getId())
				return true;

		return false;
	}
}
