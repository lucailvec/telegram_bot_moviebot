package it.unimi.di.sweng.moviebot.server;

import java.util.logging.Logger;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.service.LogService;

public class Server {

	private final Component component;

	public Server() {
		component = new Component();
		component.getServers().add(Protocol.HTTP, Configs.INSTANCE.PORT);
		component.getDefaultHost().attachDefault(new BotApplication());
		component.setLogService(new LogService(false)); // disable access
														// logging
	}

	public void start() throws Exception {
		component.start();
	}

	public void stop() throws Exception {
		component.stop();
	}

	public static void main(String[] args) throws Exception {
		Logger.getLogger(Server.class.getName()).info(String.format("Server configured at http://localhost:%d/bot/%s\n",
				Configs.INSTANCE.PORT, Configs.INSTANCE.SERVER_TOKEN));
		final Server server = new Server();
		server.start();
	}
}
