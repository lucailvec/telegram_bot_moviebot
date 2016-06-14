package it.unimi.di.sweng.moviebot.states;

import java.util.logging.Logger;

import com.pengrad.telegrambot.model.Message;

public interface BotState {
	public String handle(Logger logger, Message message);
}
