package xyz.kyngs.herbot.handlers.command.commands.info;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xyz.kyngs.herbot.HerBot;
import xyz.kyngs.herbot.handlers.command.AbstractCommand;
import xyz.kyngs.herbot.handlers.command.argument.Arguments;
import xyz.kyngs.herbot.handlers.user.UserProfile;
import xyz.kyngs.herbot.util.embed.EmbedHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelpCommand extends AbstractCommand {

    public HelpCommand(HerBot herBot, String description) {
        super(herBot, description, "");
    }

    @Override
    public void exec(User author, Guild guild, TextChannel channel, Message message, Arguments args, UserProfile profile, GuildMessageReceivedEvent event) {
        var commandHandler = herBot.getCommandHandler();

        var out = new MessageBuilder();
        var embed = EmbedHelper.GREEN.prepare(author);

        embed.setTitle("Všechny příkazy: ");

        var mergeMap = new HashMap<AbstractCommand, List<String>>();

        for (Map.Entry<String, AbstractCommand> entry : commandHandler.getCommands().entrySet()) {
            mergeMap.computeIfAbsent(entry.getValue(), k -> new ArrayList<>()).add(entry.getKey());
        }

        for (Map.Entry<AbstractCommand, List<String>> entry : mergeMap.entrySet()) {
            if (!profile.hasPermission(entry.getKey().getPermission())) continue;
            var name = new StringBuilder();
            for (var s : entry.getValue()) {
                name.append(s).append(", ");
            }
            name.delete(name.length() - 2, name.length());
            embed.addField(name.toString(), entry.getKey().getDescription(), false);
        }

        out.setEmbed(embed.build());

        message.reply(out.build()).mentionRepliedUser(false).queue();

    }
}
