package xyz.kyngs.herbot.handlers.command.commands.animal;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xyz.kyngs.herbot.HerBot;
import xyz.kyngs.herbot.handlers.command.AbstractCommand;
import xyz.kyngs.herbot.handlers.command.argument.Arguments;
import xyz.kyngs.herbot.handlers.user.UserProfile;
import xyz.kyngs.herbot.util.ExecutionResult;
import xyz.kyngs.herbot.util.MessageUtil;

public class CatCommand extends AbstractCommand {

    public CatCommand(HerBot herBot, String description) {
        super(herBot, description, "");
    }

    @Override
    public ExecutionResult exec(User author, Guild guild, TextChannel channel, Message message, Arguments args, UserProfile profile, GuildMessageReceivedEvent event) {
        MessageUtil.replyWhenArrive(herBot.getAnimalUtil().readJsonURL("https://api.thecatapi.com/api/images/get?format=json", "OOF! Nepodařilo se načíst kočičku :("), message, "Načítání kočičky <:PauseChamp:811729611079024731>");
        return ExecutionResult.SUCCESS;
    }
}
