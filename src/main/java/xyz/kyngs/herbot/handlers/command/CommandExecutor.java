package xyz.kyngs.herbot.handlers.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface CommandExecutor {

    void onCommand(User author, Guild guild, TextChannel channel, Message message, String[] args, GuildMessageReceivedEvent event);

}
