package it.unimi.di.sweng.moviebot.states;

import java.util.List;
import java.util.logging.Logger;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.pengrad.telegrambot.model.Message;

import it.unimi.di.sweng.moviebot.client.MovieClientFactory;
import it.unimi.di.sweng.moviebot.commands.MovieSearchCommand;
import it.unimi.di.sweng.moviebot.model.UsersInfo;

public class SelectionState implements BotState {
	private MovieSearchCommand toExecute;
	private List<MovieInfo> options;

	public SelectionState(List<MovieInfo> options, MovieSearchCommand toExecute) {
		if (options.size() < 2)
			throw new IllegalArgumentException("A selection with less than two elements isn't a selection!");

		this.options = options;
		this.toExecute = toExecute;
	}

	@Override
	public String handle(Logger logger, Message message) {
		String text = message.text();
		int userID = message.from().id();
		long chatID = message.from().id();

		if (text.equals("/cancel")) {
			logger.info("User " + userID + " canceled selection.");
			UsersInfo.INSTANCE.setChatState(chatID, new InitialState(new MovieClientFactory()));
			return "Selection has been canceled.";
		}

		String reply;

		try {
			int selection = Integer.parseInt(text.replaceFirst("/", ""));
			MovieInfo selectedMovie = options.get(selection - 1);

			reply = toExecute.response(userID, selectedMovie);

			UsersInfo.INSTANCE.setChatState(chatID, new InitialState(new MovieClientFactory()));
			logger.info("Executed command for user " + userID + ": " + text);
		} catch (IndexOutOfBoundsException | NumberFormatException ex) {
			reply = "Please enter a number between 1 and " + options.size();
			logger.info("User " + userID + " entered invalid selection: " + text);
		} catch (MovieDbException ex) {
			reply = "There was a problem with TheMovieDB, please try again later.\n" + ex.getMessage();
			logger.warning("MovieDB error: " + ex.getMessage());
		}

		return reply;
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof SelectionState) {
			SelectionState that = (SelectionState) arg0;
			return that.options.equals(this.options) && that.toExecute.getClass().equals(this.toExecute.getClass());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return options.hashCode() + toExecute.hashCode();
	}
}
