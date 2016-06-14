package it.unimi.di.sweng.moviebot.server;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.rules.Timeout;
import org.restlet.resource.ResourceException;

public class ServerTest {

	private static final String CORRECT_URL = "/bot/" + Configs.INSTANCE.SERVER_TOKEN;

	@Rule
	public Timeout globalTimeout = Timeout.seconds(25); // 10 seconds max

	@Rule
	public SystemOutRule stdout = new SystemOutRule().enableLog(); // 10 seconds
																	// max

	private static Server server;
	private static MockTelegram mockTelegram;

	@BeforeClass
	public static void setUp() throws Exception {
		mockTelegram = new MockTelegram();
		server = new Server();
		server.start();

	}

	@AfterClass
	public static void tearDown() throws Exception {
		server.stop();
	}

	@Test(expected = ResourceException.class)
	public void wrongToken() throws ResourceException, IOException {
		String jsonString = UpdateBuilder.getMessage("pippo23", 183686233, 183686233, "/start ciao", 41);
		mockTelegram.post("/bot/" + "tokenErrato", jsonString);
	}

	@Test
	public void wrongUpdate() throws ResourceException, IOException {
		try {
			mockTelegram.post(CORRECT_URL, "{\"Wrong JSON\": 923652896}");
		} catch (Exception e) {
			assertEquals(
					"org.restlet.resource.ResourceException: Bad Request (400) - The request could not be understood by the server due to malformed syntax",
					e.toString());
		}
	}
}
