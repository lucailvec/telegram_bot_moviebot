package it.unimi.di.sweng.moviebot.server;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import it.unimi.di.sweng.moviebot.server.BotResource;

public class BotApplication extends Application {

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attach("/bot/{token}", BotResource.class);
		return router;
	}
}
