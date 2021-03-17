package xyz.kyngs.herbot.handlers.command.commands.admin;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xyz.kyngs.herbot.HerBot;
import xyz.kyngs.herbot.handlers.command.AbstractCommand;
import xyz.kyngs.herbot.handlers.user.UserProfile;
import xyz.kyngs.herbot.util.MemberUtil;

import java.awt.*;

public class PermissionCommand extends AbstractCommand {

    public PermissionCommand(HerBot herBot, String description) {
        super(herBot, description, "permissions");
        addArg(String.class, "give", "take");
        addArg(String.class, "uživatel");
        addArg(String.class, "oprávnění");
    }

    @Override
    public void exec(User author, Guild guild, TextChannel channel, Message message, String[] args, UserProfile profile, GuildMessageReceivedEvent event) {
        User user = MemberUtil.getUser(args[1], herBot.getJda());
        if (user == null) {
            message.reply("Uživatel nenalezen!").mentionRepliedUser(false).queue();
            return;
        }
        var targetProfile = herBot.getUserHandler().getUser(user.getId());
        if (args[0].contentEquals("give")) {
            targetProfile.allowPermission(args[2]);
        } else {
            targetProfile.declinePermission(args[2]);
        }
        herBot.getUserHandler().saveUser(targetProfile, user.getId());
        var builder = new EmbedBuilder();
        builder.setColor(Color.GREEN);
        builder.setTitle("Úspěch");
        builder.setDescription("Oprávnění úspěšně aktualizováno");
        message.reply(builder.build()).mentionRepliedUser(false).queue();
    }
}
