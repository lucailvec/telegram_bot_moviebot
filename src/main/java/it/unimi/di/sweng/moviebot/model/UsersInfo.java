package it.unimi.di.sweng.moviebot.model;

import java.util.HashMap;
import java.util.Map;

import it.unimi.di.sweng.moviebot.client.MovieClientFactory;
import it.unimi.di.sweng.moviebot.model.MovieReferences.MovieReference;
import it.unimi.di.sweng.moviebot.states.BotState;
import it.unimi.di.sweng.moviebot.states.InitialState;

public enum UsersInfo {
	INSTANCE;

	private final static Map<Integer, UserMovies> INFO = new HashMap<Integer, UserMovies>();
	private final static Map<Long, BotState> SESSIONS = new HashMap<Long, BotState>();

	public synchronized void clearInfo() {
		INFO.clear();
		SESSIONS.clear();
	}

	private void createMoviesIfNotFound(int userID) {
		if (!INFO.containsKey(userID))
			INFO.put(userID, new UserMovies());
	}

	private void createSessionIfNotFound(long chatID) {
		if (!SESSIONS.containsKey(chatID))
			SESSIONS.put(chatID, new InitialState(new MovieClientFactory()));
	}

	public synchronized BotState getChatState(long chatID) {
		createSessionIfNotFound(chatID);
		return SESSIONS.get(chatID);
	}

	public synchronized void setChatState(long chatID, BotState state) {
		SESSIONS.put(chatID, state);
	}

	public synchronized UserMovies getUserMovies(int userID) {
		createMoviesIfNotFound(userID);
		return INFO.get(userID);
	}

	public synchronized void watch(int userID, MovieReference movie) {
		createMoviesIfNotFound(userID);
		INFO.get(userID).watch(movie);
	}

	public synchronized void unwatch(int userID, MovieReference movie) {
		createMoviesIfNotFound(userID);
		INFO.get(userID).unwatch(movie);
	}

	public synchronized void blacklist(int userID, MovieReference movie) {
		createMoviesIfNotFound(userID);
		INFO.get(userID).blacklist(movie);
	}

	public synchronized void unblacklist(int userID, MovieReference movie) {
		createMoviesIfNotFound(userID);
		INFO.get(userID).unblacklist(movie);
	}

	public synchronized void addToWatchlist(int userID, MovieReference movie) {
		createMoviesIfNotFound(userID);
		INFO.get(userID).addToWatchlist(movie);
	}

	public synchronized void removeFromWatchlist(int userID, MovieReference movie) {
		createMoviesIfNotFound(userID);
		INFO.get(userID).removeFromWatchlist(movie);
	}
}
