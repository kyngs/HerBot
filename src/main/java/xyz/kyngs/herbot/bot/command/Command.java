package xyz.kyngs.herbot.bot.command;

import co.aikar.commands.BaseCommand;
import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import org.bson.Document;
import xyz.kyngs.herbot.bot.HerBot;
import xyz.kyngs.herbot.bot.HerBotDaemon;

import java.util.concurrent.CompletableFuture;

public class Command<T extends HerBotDaemon> extends BaseCommand {

    protected final HerBot bot;
    protected final T parent;
    protected final CommandDaemon commandDaemon;

    public Command(T parent) {
        bot = parent.getBot();
        this.parent = parent;
        commandDaemon = parent.getCommandDaemon();
    }

    public void replyWhenArrive(CompletableFuture<String> future, Message original, String message) {
        original.reply(message).mentionRepliedUser(false).queue(sentMessage -> {
            future.whenCompleteAsync((s, throwable) -> {
                sentMessage.editMessage(s).queue();
            });
        });
    }

    protected Document getUser(Member member) {
        return commandDaemon.getDatabaseDaemon().getUser(member.getIdLong());
    }

    protected MongoCollection<Document> getCollection() {
        return commandDaemon.getDatabaseDaemon().getUserCollection();
    }

}
