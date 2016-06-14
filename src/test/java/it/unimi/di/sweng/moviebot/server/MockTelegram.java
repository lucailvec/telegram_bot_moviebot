package it.unimi.di.sweng.moviebot.server;

import java.io.IOException;

import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import it.unimi.di.sweng.moviebot.server.Configs;

public class MockTelegram {
	private final String serverUrl;

	public MockTelegram() {
		this.serverUrl = "http://localhost:" + Configs.INSTANCE.PORT;
	}

	public String post(final String url, final String mex) throws ResourceException, IOException {
		final ClientResource clientResource = new ClientResource(serverUrl + url);
		return clientResource.post(mex).getText();
	}
}
