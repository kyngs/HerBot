package xyz.kyngs.herbot.handlers.command.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xyz.kyngs.herbot.HerBot;
import xyz.kyngs.herbot.handlers.command.AbstractCommand;

import java.awt.*;

public class InfoCommand extends AbstractCommand {
    public InfoCommand(HerBot herBot, String description) {
        super(herBot, description);
    }

    @Override
    public void onCommand(User author, Guild guild, TextChannel channel, Message message, String[] args, GuildMessageReceivedEvent event) {
        var builder = new EmbedBuilder();

        builder.setTitle("Informace");
        builder.addField("Verze:", "1.1", false);
        builder.addField("Máš nějaký dotaz k botovi, nebo jsi našel chybu? Zkontaktuj mě.", "Discord: kyngs#0666", false);
        builder.setColor(Color.GREEN);

        message.reply(builder.build()).mentionRepliedUser(false).queue();

    }
}
