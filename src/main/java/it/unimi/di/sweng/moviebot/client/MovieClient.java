package it.unimi.di.sweng.moviebot.client;

import java.time.LocalDate;
import java.util.List;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.model.media.Video;
import com.omertron.themoviedbapi.model.movie.MovieBasic;
import com.omertron.themoviedbapi.model.movie.MovieInfo;

import it.unimi.di.sweng.moviebot.model.MovieReferences.MovieReference;

public interface MovieClient {
	public List<MovieInfo> searchMovie(String name, int resultCount) throws MovieDbException;

	public MovieInfo retrieveMovie(int id) throws MovieDbException;

	public List<Video> retrieveMovieVideos(int id) throws MovieDbException;

	public List<MovieBasic> retrievePopulars(int movieCount, List<MovieReference> excludedMovies)
			throws MovieDbException;

	public List<MovieBasic> retrievePopularsByGenre(String genre, int movieCount, List<MovieReference> excludedMovies)
			throws MovieDbException;

	public List<MovieBasic> retrievePopularsAfter(LocalDate date, int movieCount, List<MovieReference> excludedMovies)
			throws MovieDbException;
}
