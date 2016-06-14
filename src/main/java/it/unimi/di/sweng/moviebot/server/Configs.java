package it.unimi.di.sweng.moviebot.server;

import java.util.logging.Logger;

public enum Configs {
	INSTANCE;

	private static final int DEFAULT_SERVER_PORT = 8080;
	public final int PORT;
	public final String SERVER_TOKEN;
	public final String BOT_TOKEN;
	public final String MOVIEDB_API_KEY;
	public final boolean TESTING_PHASE;

	private Configs() {
		int temp;
		try {
			temp = Integer.parseInt(System.getenv("PORT"));
		} catch (NumberFormatException e) {
			Logger.getLogger(Configs.class.getName()).warning("Error while retrieving port from sysenv");
			temp = DEFAULT_SERVER_PORT;
		}

		PORT = temp;
		SERVER_TOKEN = System.getenv("TELEGRAM_SERVER_TOKEN");
		BOT_TOKEN = System.getenv("TELEGRAM_BOT_TOKEN");
		MOVIEDB_API_KEY = System.getenv("MOVIEDB_API_KEY");

		// check if it's set, I don't care about the value
		TESTING_PHASE = System.getenv("TELEGRAM_BOT_TESTING") != null;
	}
}
