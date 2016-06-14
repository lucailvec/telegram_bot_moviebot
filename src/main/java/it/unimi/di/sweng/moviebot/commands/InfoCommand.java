package it.unimi.di.sweng.moviebot.commands;

import com.omertron.themoviedbapi.MovieDbException;
import com.pengrad.telegrambot.model.Message;

public class InfoCommand implements BotCommand {

	@Override
	public String response(Message message) throws MovieDbException {
		return "This bot was developed as software engeneering course project by unimi students.\n"
				+ "It uses TheMovieDB to retrive informations (www.themoviedb.org).";
	}

	@Override
	public String hook() {
		return "info";
	}

	@Override
	public String description() {
		return "shows information about this bot.";
	}

}