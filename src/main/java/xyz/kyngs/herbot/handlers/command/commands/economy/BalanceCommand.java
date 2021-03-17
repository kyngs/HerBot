package xyz.kyngs.herbot.handlers.command.commands.economy;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xyz.kyngs.herbot.HerBot;
import xyz.kyngs.herbot.handlers.command.AbstractCommand;
import xyz.kyngs.herbot.handlers.user.UserProfile;

import java.awt.*;

public class BalanceCommand extends AbstractCommand {
    public BalanceCommand(HerBot herBot, String description) {
        super(herBot, description, "");
    }

    @Override
    public void exec(User author, Guild guild, TextChannel channel, Message message, String[] args, UserProfile profile, GuildMessageReceivedEvent event) {
        var builder = new EmbedBuilder();
        builder.setColor(Color.GREEN);
        builder.setTitle(String.format("Na účtě máš %s coinů", profile.getCoins()));
        message.reply(builder.build()).mentionRepliedUser(false).queue();
    }
}
