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
import xyz.kyngs.herbot.handlers.user.UserProfile;
import xyz.kyngs.herbot.util.embed.EmbedHelper;

import java.time.LocalDate;
import java.time.Period;

public class DailyCommand extends AbstractCommand {

    public DailyCommand(HerBot herBot, String description) {
        super(herBot, description, "");
    }

    @Override
    public void exec(User author, Guild guild, TextChannel channel, Message message, Arguments args, UserProfile profile, GuildMessageReceivedEvent event) {

        var today = LocalDate.now();
        var period = Period.between(today, profile.getLastDailyClaim());
        var days = Math.abs(period.getDays());

        EmbedBuilder builder;
        if (days == 0) {

            builder = EmbedHelper.RED.prepare(author);
            builder.setTitle("Woah zpomal");

            builder.setDescription("Dnešní daily sis už vyzvedl, přijď zítra!");


        } else {

            builder = EmbedHelper.GREEN.prepare(author);
            builder.setTitle("Tady máš coiny");

            var toAdd = 2000;

            if (days >= 2) {
                profile.setDailyStreak(0);
            } else {
                profile.setDailyStreak(profile.getDailyStreak() + 1);
            }

            toAdd += profile.getDailyStreak() * 200;

            builder.setDescription(String.format("Přičteno %s coinů na tvůj účet!", toAdd));
            builder.setFooter(String.format("Streak %s dnů (+%s coinů)", profile.getDailyStreak(), profile.getDailyStreak() * 200));
            profile.setLastDailyClaim(LocalDate.now());
            profile.addCoins(toAdd);


        }
        message.reply(builder.build()).mentionRepliedUser(false).queue();

    }
}
