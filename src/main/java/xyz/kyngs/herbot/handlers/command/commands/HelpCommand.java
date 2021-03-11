package xyz.kyngs.herbot.handlers.command.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xyz.kyngs.herbot.HerBot;
import xyz.kyngs.herbot.handlers.command.AbstractCommand;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelpCommand extends AbstractCommand {

    public HelpCommand(HerBot herBot, String description) {
        super(herBot, description);
    }

    @Override
    public void onCommand(User author, Guild guild, TextChannel channel, Message message, String[] args, GuildMessageReceivedEvent event) {
        var commandHandler = herBot.getCommandHandler();

        var out = new MessageBuilder();
        var embed = new EmbedBuilder();

        embed.setTitle("Všechny příkazy: ");
        embed.setColor(Color.GREEN);

        var mergeMap = new HashMap<AbstractCommand, List<String>>();

        for (Map.Entry<String, AbstractCommand> entry : commandHandler.getCommands().entrySet()) {
            mergeMap.computeIfAbsent(entry.getValue(), k -> new ArrayList<>()).add(entry.getKey());
        }

        for (Map.Entry<AbstractCommand, List<String>> entry : mergeMap.entrySet()) {
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
