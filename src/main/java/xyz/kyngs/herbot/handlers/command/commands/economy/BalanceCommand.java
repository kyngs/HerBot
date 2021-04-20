package xyz.kyngs.herbot.handlers.command.commands.economy;

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
import xyz.kyngs.herbot.util.embed.EmbedHelper;

public class BalanceCommand extends AbstractCommand {
    public BalanceCommand(HerBot herBot, String description) {
        super(herBot, description, "");
    }

    @Override
    public ExecutionResult exec(User author, Guild guild, TextChannel channel, Message message, Arguments args, UserProfile profile, GuildMessageReceivedEvent event) {
        var builder = EmbedHelper.GREEN.prepare(author);
        builder.setTitle(String.format("Na účtě máš %s coinů", profile.getCoins()));
        message.reply(builder.build()).mentionRepliedUser(false).queue();
        return ExecutionResult.SUCCESS;
    }
}
