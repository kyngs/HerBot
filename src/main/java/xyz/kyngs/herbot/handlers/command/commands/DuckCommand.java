package xyz.kyngs.herbot.handlers.command.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xyz.kyngs.herbot.HerBot;
import xyz.kyngs.herbot.handlers.command.AbstractCommand;
import xyz.kyngs.herbot.util.AnimalUtil;
import xyz.kyngs.herbot.util.MessageUtil;

public class DuckCommand extends AbstractCommand {

    public DuckCommand(HerBot herBot, String description) {
        super(herBot, description);
    }

    @Override
    public void onCommand(User author, Guild guild, TextChannel channel, Message message, String[] args, GuildMessageReceivedEvent event) {
        MessageUtil.replyWhenArrive(AnimalUtil.getDuck(), message, "Načítání kachničky <:PauseChamp:811729611079024731>");
    }
}
