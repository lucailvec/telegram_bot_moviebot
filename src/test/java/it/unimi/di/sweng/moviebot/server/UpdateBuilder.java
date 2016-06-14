package it.unimi.di.sweng.moviebot.server;

public class UpdateBuilder {

	public static String getMessage(String user, int userId, long chatId, String mex, int messageId) {
		StringBuilder builder = new StringBuilder();
		builder.append("{\"" + "update_id" + "\": " + 729787354 + ",");
		builder.append("\"message\":" + formatMessage(user, userId, chatId, mex, messageId));
		builder.append("}");
		return builder.toString();

	}

	private static String formatMessage(String user, int userId, long chatId, String mex, int messageId) {
		StringBuilder message = new StringBuilder();
		message.append("{\"" + "message_id" + "\": " + messageId + ",");
		message.append("\"" + "from" + "\": " + formatFrom(user, userId) + ",");
		message.append("\"" + "chat" + "\": " + formatPrivateChat(user, chatId) + ",");
		message.append("\"" + "date" + "\": " + 1464970145 + ",");
		message.append("\"" + "text" + "\": \"" + mex + "\"");
		message.append("}");
		return message.toString();
	}

	private static String formatFrom(String user, int userId) {
		StringBuilder obj = new StringBuilder();
		obj.append("{\"" + "id" + "\": " + userId + ",");
		obj.append("\"" + "first_name" + "\": \"" + "first_name" + "\",");
		obj.append("\"" + "last_name" + "\": \"" + "last_name" + "\",");
		obj.append("\"" + "username" + "\": \"" + user + "\"");
		obj.append("}");
		return obj.toString();
	}

	private static String formatPrivateChat(String user, long chatId) {

		StringBuilder obj = new StringBuilder();
		obj.append("{\"" + "id" + "\": " + chatId + ",");
		obj.append("\"" + "first_name" + "\": \"" + "first_name" + "\",");
		obj.append("\"" + "last_name" + "\": \"" + "last_name" + "\",");
		obj.append("\"" + "username" + "\": \"" + user + "\",");
		obj.append("\"" + "type" + "\": \"" + "private" + "\"");
		obj.append("}");
		return obj.toString();
	}

	public static String parseMessage(String stringRapresentation) {
		return stringRapresentation.split("text='")[1].split("'")[0];
	}
}
