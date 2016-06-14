package it.unimi.di.sweng.moviebot.commands;

import com.omertron.themoviedbapi.MovieDbException;
import com.pengrad.telegrambot.model.Message;

public class StartCommand implements BotCommand {

	@Override
	public String response(Message message) throws MovieDbException {
		return "Welcome to MovieBot! This bot provides support to discover new movies using TheMovieDB.";
	}

	@Override
	public String hook() {
		return "start";
	}

	@Override
	public String description() {
		return "Displays a welcome message.";
	}

}
