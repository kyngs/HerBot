package xyz.kyngs.herbot.handlers.command.commands.economy;

import net.dv8tion.jda.api.EmbedBuilder;
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
import xyz.kyngs.herbot.handlers.user.UserProfile;
import xyz.kyngs.herbot.util.ExecutionResult;
import xyz.kyngs.herbot.util.embed.EmbedHelper;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public class GambleCommand extends AbstractCommand {

    public GambleCommand(HerBot herBot, String description) {
        super(herBot, description, "", Duration.ofSeconds(5));
        addArg(new IntegerArgument(NumberState.POSITIVE, "sázka"));
    }

    @Override
    public ExecutionResult exec(User author, Guild guild, TextChannel channel, Message message, Arguments args, UserProfile profile, GuildMessageReceivedEvent event) {
        var random = ThreadLocalRandom.current();

        int amount = args.getArgument(0);

        if (amount > profile.getCoins()) {
            var builder = EmbedHelper.TOO_POOR.prepare(author);
            message.reply(builder.build()).mentionRepliedUser(false).queue();
            return ExecutionResult.FAILURE;
        }

        boolean win = random.nextInt(100) > 50;

        EmbedBuilder builder;
        if (win) {
            builder = EmbedHelper.GREEN.prepare(author);
            builder.setTitle("Výhra");
            builder.setDescription(String.format("Na účet ti bylo přidáno %s coinů", amount));
            profile.addCoins(amount);
        } else {
            builder = EmbedHelper.RED.prepare(author);
            builder.setTitle("Prohra");
            builder.setDescription(String.format("Z účtu ti bylo odebráno %s coinů", amount));
            profile.removeCoins(amount);
        }
        message.reply(builder.build()).mentionRepliedUser(false).queue();

        return ExecutionResult.SUCCESS;

    }
}
