package it.unimi.di.sweng.moviebot.server;

import java.io.IOException;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import it.unimi.di.sweng.moviebot.model.UsersInfo;

public class BotResource extends ServerResource {
	@Post
	public Representation update(Representation data) throws IOException {

		// check that Telegram is using the server token

		final String token = getAttribute("token");
		if (!Configs.INSTANCE.SERVER_TOKEN.equals(token)) {
			setStatus(Status.CLIENT_ERROR_FORBIDDEN, "Wrong server token");
			getLogger().warning("User tried to connect with an incorrect token.");
			return null;
		}

		// get the update and try to parse it

		final Update update = BotUtils.parseUpdate(data.getText());
		if (update.updateId() == null) {
			getLogger().warning("Can't parse update, text was: \"" + data.getText() + "\"");
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "Can't parse the update");
			return null;
		}

		getLogger().info("<= " + update);

		final Message message = update.message();
		final Chat chat = message.chat();
		
		String reply = "";
		
		try {
			reply = UsersInfo.INSTANCE.getChatState(chat.id()).handle(getLogger(), message);
		} catch (Exception ex) {
			reply = "The server encountered an unexpected problem during execution.";
			getLogger().severe("Unexpected exception during execution: " + ex);
		}

		final TelegramBot bot = TelegramBotAdapter.build(Configs.INSTANCE.BOT_TOKEN);
		final SendResponse response = bot.execute(new SendMessage(chat.id(), reply));
		getLogger().info("=> " + response);

		return null;
	}
}
