package xyz.kyngs.herbot.handlers.command;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xyz.kyngs.herbot.HerBot;
import xyz.kyngs.herbot.handlers.AbstractHandler;
import xyz.kyngs.herbot.util.embed.EmbedHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler extends AbstractHandler {

    private final Map<String, AbstractCommand> commands;
    private final String spamChannel;

    public CommandHandler(HerBot herBot) {
        super(herBot);

        commands = new HashMap<>();
        var configuration = herBot.getConfiguration();
        spamChannel = configuration.getString("spam_channel");
    }

    public void processNewMessage(GuildMessageReceivedEvent event) {

        var message = event.getMessage();

        if (!event.getChannel().getId().contentEquals(spamChannel) || !message.getContentRaw().startsWith(".")) return;

        var text = message.getContentRaw();
        text = text.substring(1);

        for (User user : message.getMentionedUsers()) {
            text = text.replace(String.format("<@!%s>", user.getId()), "");
        }

        for (TextChannel channel : message.getMentionedChannels()) {
            text = text.replace(String.format("<#%s>", channel.getId()), "");
        }

        for (Role role : message.getMentionedRoles()) {
            text = text.replace(String.format("<@&%s>", role.getId()), "");
        }

        var chars = text.toCharArray();

        var wasSpace = false;

        var stringBuilder = new StringBuilder();

        for (char c : chars) {
            if (c == ' ') {
                if (wasSpace) continue;
                wasSpace = true;
            } else {
                wasSpace = false;
            }

            stringBuilder.append(c);

        }

        text = stringBuilder.toString();

        var args = text.split(" ");

        if (args.length <= 0) return;

        var command = commands.get(args[0]);

        if (command == null) {
            message.reply("Tento příkaz neznám! Pokud jsi ztracen napiš .help").mentionRepliedUser(false).queue();
            return;
        }

        try {
            var userHandler = herBot.getUserHandler();
            var userID = event.getAuthor().getId();
            var profile = userHandler.getUser(userID);
            command.onCommand(event.getAuthor(), event.getGuild(), event.getChannel(), message, Arrays.copyOfRange(args, 1, args.length), profile, event);
            userHandler.saveUser(profile, userID);
        } catch (Exception e) {
            event.getChannel().sendMessage(EmbedHelper.RED.prepare(event.getAuthor())
                    .setTitle("Chyba!")
                    .setDescription("Nastala chyba při spouštění tohoto příkazu, chyba byla automaticky ohlášena! Omlouvám se za způsobené potíže.")
                    .build()
            ).queue();
            herBot.getThrowableHandler().reportThrowable(e);
        }

    }

    public void registerCommand(AbstractCommand executor, String... names) {
        for (var name : names) {
            commands.put(name, executor);
        }
    }

    public Map<String, AbstractCommand> getCommands() {
        return commands;
    }
}
