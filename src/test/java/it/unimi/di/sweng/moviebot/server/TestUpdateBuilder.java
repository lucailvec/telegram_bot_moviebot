package it.unimi.di.sweng.moviebot.server;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestUpdateBuilder {

	@Test
	public void standardMessage() {
		String expected = "{\"update_id\": 729787354,\"message\":{\"message_id\": 41,\"from\": {\"id\": 183686233,\"first_name\": \"first_name\",\"last_name\": \"last_name\",\"username\": \"pippo23\"},\"chat\": {\"id\": 183686233,\"first_name\": \"first_name\",\"last_name\": \"last_name\",\"username\": \"pippo23\",\"type\": \"private\"},\"date\": 1464970145,\"text\": \"start\"}}";
		String jsonString = UpdateBuilder.getMessage("pippo23", 183686233, 183686233, "start", 41);
		System.out.println(jsonString);
		assertEquals(expected, jsonString);
	}
}
