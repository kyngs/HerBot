package xyz.kyngs.herbot.handlers.command.commands.admin;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xyz.kyngs.herbot.HerBot;
import xyz.kyngs.herbot.handlers.command.AbstractCommand;
import xyz.kyngs.herbot.handlers.command.argument.Arguments;
import xyz.kyngs.herbot.handlers.command.argument.arguments.OptionArgument;
import xyz.kyngs.herbot.handlers.command.argument.arguments.StringArgument;
import xyz.kyngs.herbot.handlers.command.argument.arguments.UserArgument;
import xyz.kyngs.herbot.handlers.user.UserProfile;
import xyz.kyngs.herbot.util.embed.EmbedHelper;

public class PermissionCommand extends AbstractCommand {

    public PermissionCommand(HerBot herBot, String description) {
        super(herBot, description, "ManagePermissions");
        addArg(new OptionArgument("give", "take"));
        addArg(new UserArgument("uživatel"));
        addArg(new StringArgument("oprávnění"));
    }

    @Override
    public void exec(User author, Guild guild, TextChannel channel, Message message, Arguments args, UserProfile profile, GuildMessageReceivedEvent event) {
        User user = args.getArgument(1);

        var targetProfile = herBot.getUserHandler().getUser(user.getId());
        String permission = args.getArgument(2);
        String action = args.getArgument(0);

        if (herBot.getSecurityHandler().getLoadedPermissions().get(permission) == null) {
            var builder = EmbedHelper.RED.prepare(author);
            builder.setTitle("Toto oprávnění neexistuje");
            builder.setDescription("Lituji, ale toto oprávnění neexistuje, pokud chceš zobrazit všechna existující oprávnění, použij .allperms");
            message.reply(builder.build()).mentionRepliedUser(false).queue();
            return;
        }

        if (!profile.hasPermission(permission)) {
            var builder = EmbedHelper.CANNOT_PERFORM.prepare(author);
            builder.setDescription("Nemáš oprávnění pro upravování tohoto oprávnění");
            message.reply(builder.build()).mentionRepliedUser(false).queue();
            return;
        }

        if (action.contentEquals("give")) {
            targetProfile.allowPermission(permission);
        } else if (action.contentEquals("take")) {
            targetProfile.declinePermission(permission);
        }

        herBot.getUserHandler().saveUser(targetProfile, user.getId());
        var builder = EmbedHelper.SUCCESS.prepare(author);
        builder.setDescription("Oprávnění úspěšně aktualizováno");
        message.reply(builder.build()).mentionRepliedUser(false).queue();
    }
}
