package xyz.kyngs.herbot.handlers.command.commands.admin;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xyz.kyngs.herbot.HerBot;
import xyz.kyngs.herbot.handlers.command.AbstractCommand;
import xyz.kyngs.herbot.handlers.command.argument.Arguments;
import xyz.kyngs.herbot.handlers.command.argument.arguments.UserArgument;
import xyz.kyngs.herbot.handlers.user.UserProfile;
import xyz.kyngs.herbot.util.embed.EmbedHelper;

public class UserPermissions extends AbstractCommand {

    public UserPermissions(HerBot herBot, String description) {
        super(herBot, description, "ManagePermissions");
        addArg(new UserArgument("uživatel"));
    }

    @Override
    public void exec(User author, Guild guild, TextChannel channel, Message message, Arguments args, UserProfile profile, GuildMessageReceivedEvent event) {
        var builder = EmbedHelper.GREEN.prepare(author);
        builder.setTitle("Zde je seznam všech oprávnění uživatele:");
        for (var perm : herBot.getSecurityHandler().getPermissionsFromNames(herBot.getUserHandler().getUser(args.<User>getArgument(0).getId()).getPerms())) {
            builder.addField(perm.getName(), perm.getDescription(), false);
        }
        message.reply(builder.build()).mentionRepliedUser(false).queue();
    }
}
