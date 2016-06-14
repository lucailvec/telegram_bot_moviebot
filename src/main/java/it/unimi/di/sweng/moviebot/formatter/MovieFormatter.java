package it.unimi.di.sweng.moviebot.formatter;

import java.util.List;

import com.omertron.themoviedbapi.model.media.Video;
import com.omertron.themoviedbapi.model.movie.MovieBasic;
import com.omertron.themoviedbapi.model.movie.MovieInfo;

import it.unimi.di.sweng.moviebot.model.MovieReferences.MovieReference;

public interface MovieFormatter {

	public String genreReplyString(List<MovieBasic> movies);

	public String ignoreMovieReplyString(MovieInfo filmInfo);

	public String ignoredListReplyString(List<MovieReference> movies);

	public String notToWatchMovieReplyString(MovieInfo filmInfo);

	public String overviewReplyString(MovieInfo filmInfo);

	public String popularReplyString(List<MovieBasic> movies);

	public String recentReplyString(List<MovieBasic> movies);

	public String toWatchMovieReplyString(MovieInfo filmInfo);

	public String trailerReplyString(List<Video> videos);

	public String watchMovieReplyString(MovieInfo filmInfo);

	public String unwatchMovieReplyString(MovieInfo filmInfo);

	public String unignoreMovieReplyString(MovieInfo filmInfo);

	public String toWatchListReplyString(List<MovieReference> movies);

	public String watchedListReplyString(List<MovieReference> movies);

}
