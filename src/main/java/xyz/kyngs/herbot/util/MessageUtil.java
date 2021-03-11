package xyz.kyngs.herbot.util;

import net.dv8tion.jda.api.entities.Message;

public class MessageUtil {

    public static boolean checkMessageExists(Message message) {
        return message.getTextChannel().retrieveMessageById(message.getId()).complete() != null;
    }

}
