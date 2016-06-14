package it.unimi.di.sweng.moviebot.commands;

import com.omertron.themoviedbapi.MovieDbException;
import com.pengrad.telegrambot.model.Message;

public interface BotCommand {
	public String response(Message message) throws MovieDbException;

	public String hook();

	public String description();
}
