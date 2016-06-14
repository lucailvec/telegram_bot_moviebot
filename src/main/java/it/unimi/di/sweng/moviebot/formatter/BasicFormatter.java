package it.unimi.di.sweng.moviebot.formatter;

import java.util.List;

import com.omertron.themoviedbapi.model.media.Video;
import com.omertron.themoviedbapi.model.movie.MovieBasic;
import com.omertron.themoviedbapi.model.movie.MovieInfo;

import it.unimi.di.sweng.moviebot.model.MovieReferences.MovieReference;

public class BasicFormatter implements MovieFormatter {

	public final String YT_URL = "https://www.youtube.com/watch?v=";

	private String movieListReplyString(List<MovieBasic> movies, String header, String emptyListReply) {
		if (movies.size() == 0)
			return emptyListReply;
		
		StringBuilder reply = new StringBuilder(header+"\n");
		for (MovieBasic movie : movies) 
			reply.append("- " + movie.getTitle() + "\n");
		
		return reply.toString();
	}
	
	private String movieRefListReplyString(List<MovieReference> movies, String header, String emptyListReply) {
		if (movies.size() == 0)
			return emptyListReply;
		
		StringBuilder reply = new StringBuilder(header+"\n");
		for (MovieReference movie : movies)
			reply.append("- " + movie.TITLE + "\n");
		
		return reply.toString();
	}
	
	@Override
	public String genreReplyString(List<MovieBasic> movies) {
		return movieListReplyString(movies, "Popular movies for the given genre are :", "No popular movies found for the given genre.");
	}

	@Override
	public String ignoreMovieReplyString(MovieInfo filmInfo) {
		return "The movie \"" + filmInfo.getTitle() + "\" has been added to the blacklist.";
	}

	@Override
	public String ignoredListReplyString(List<MovieReference> movies) {
		return movieRefListReplyString(movies, "The movies in your blacklist are :", "There are no movies in your blacklist.");
	}

	@Override
	public String notToWatchMovieReplyString(MovieInfo filmInfo) {
		return "The movie \"" + filmInfo.getTitle() + "\" has been removed from the watchlist.";
	}

	@Override
	public String overviewReplyString(MovieInfo filmInfo) {
		return "About the movie :\n" + filmInfo.getOverview();
	}

	@Override
	public String popularReplyString(List<MovieBasic> movies) {
		return movieListReplyString(movies, "The most popular movies at the moment are :", "No popular movies found.");
	}

	@Override
	public String recentReplyString(List<MovieBasic> movies) {
		return movieListReplyString(movies, "Popular movies released after the given date are :", "No popular movies released after the given date found.");
	}

	@Override
	public String toWatchMovieReplyString(MovieInfo filmInfo) {
		return "The movie \"" + filmInfo.getTitle() + "\" has been added to the watchlist."; 
	}

	@Override
	public String trailerReplyString(List<Video> videos) {
		String s="The available trailers on YouTube are :\n";
		int count = 0;
		for (int i = 0; i < Math.min(videos.size(), 3); i++) {
			if (videos.get(i).getSite().equalsIgnoreCase("YouTube")) {
				count++;
				s += "- " + YT_URL + videos.get(i).getKey() + "\n";
			}
		}
		if (count == 0)
			s = "There are no trailer video.";
		return s;
	}

	@Override
	public String watchMovieReplyString(MovieInfo filmInfo) {
		return "The movie \"" + filmInfo.getTitle() + "\" has been added to the watched list.";
	}

	@Override
	public String unwatchMovieReplyString(MovieInfo filmInfo) {
		return "The movie \"" + filmInfo.getTitle() + "\" has been removed from the watched list.";
	}

	@Override
	public String unignoreMovieReplyString(MovieInfo filmInfo) {
		return "The movie \"" + filmInfo.getTitle() + "\" has been removed from the blacklist.";
	}

	@Override
	public String toWatchListReplyString(List<MovieReference> movies) {
		return movieRefListReplyString(movies, "The movies on your watchlist are :", "There are no movies on your watchlist.");
	}

	@Override
	public String watchedListReplyString(List<MovieReference> movies) {
		return movieRefListReplyString(movies, "Movies you've already watched :", "You haven't watched any movies yet.");
	}

}
