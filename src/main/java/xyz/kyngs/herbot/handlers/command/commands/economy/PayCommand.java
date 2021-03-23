package xyz.kyngs.herbot.handlers.command.commands.economy;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xyz.kyngs.herbot.HerBot;
import xyz.kyngs.herbot.handlers.command.AbstractCommand;
import xyz.kyngs.herbot.handlers.command.argument.Arguments;
import xyz.kyngs.herbot.handlers.command.argument.NumberState;
import xyz.kyngs.herbot.handlers.command.argument.arguments.IntegerArgument;
import xyz.kyngs.herbot.handlers.command.argument.arguments.UserArgument;
import xyz.kyngs.herbot.handlers.user.UserProfile;
import xyz.kyngs.herbot.util.embed.EmbedHelper;

public class PayCommand extends AbstractCommand {

    public PayCommand(HerBot herBot, String description) {
        super(herBot, description, "");
        addArg(new UserArgument("uživatel"));
        addArg(new IntegerArgument(NumberState.POSITIVE, "částka"));
    }

    @Override
    public void exec(User author, Guild guild, TextChannel channel, Message message, Arguments args, UserProfile profile, GuildMessageReceivedEvent event) {
        User user = args.getArgument(0);
        int amount = args.getArgument(1);

        if (amount > profile.getCoins()) {
            var builder = EmbedHelper.TOO_POOR.prepare(author);
            message.reply(builder.build()).mentionRepliedUser(false).queue();
            return;
        }

        var targetProfile = herBot.getUserHandler().getUser(user.getId());
        profile.removeCoins(amount);
        targetProfile.addCoins(amount);
        herBot.getUserHandler().saveUser(targetProfile, user.getId());

        var builder = EmbedHelper.SUCCESS.prepare(author);

        builder.setDescription("Coiny odeslány");

        message.reply(builder.build()).mentionRepliedUser(false).queue();
    }
}
