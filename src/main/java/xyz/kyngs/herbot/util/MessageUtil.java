package xyz.kyngs.herbot.util;

import net.dv8tion.jda.api.entities.Message;

import java.util.concurrent.CompletableFuture;

public class MessageUtil {

    public static boolean checkMessageExists(Message message) {
        return message.getTextChannel().retrieveMessageById(message.getId()).complete() != null;
    }

    public static void replyWhenArrive(CompletableFuture<String> future, Message original, String message) {
        original.reply(message).mentionRepliedUser(false).queue(sentMessage -> {
            future.whenCompleteAsync((s, throwable) -> {
                sentMessage.editMessage(s).queue();
                ;
            });
        });
    }

}
