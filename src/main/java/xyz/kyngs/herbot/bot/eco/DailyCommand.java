package xyz.kyngs.herbot.bot.eco;

import co.aikar.commands.JDACommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import xyz.kyngs.herbot.bot.command.EmbedHelper;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

@CommandAlias("daily")
@Description("Dá ti dnešní odměnu")
public class DailyCommand extends EconomyCommand {
    public DailyCommand(EconomyDaemon parent) {
        super(parent);
    }

    @Default
    public void daily(Member member, JDACommandIssuer issuer) {
        var user = getUser(member);
        var author = member.getUser();

        var today = LocalDate.now();
        var period = Period.between(today, LocalDate.from(user.getDate("last-claim").toInstant().atZone(ZoneId.ofOffset("UTC", ZoneOffset.UTC))));
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

            var streak = user.getInteger("streak", -1);

            if (days >= 2) {

                streak = 0;
            } else {
                streak++;
            }

            toAdd += streak * 200;

            builder.setDescription(String.format("Přičteno %s coinů na tvůj účet!", toAdd));
            builder.setFooter(String.format("Streak %s dnů (+%s coinů)", streak, streak * 200));

            getCollection().updateOne(Filters.eq("_id", member.getIdLong()), Updates.combine(
                    Updates.inc("balance", toAdd),
                    Updates.set("last-claim", new Date()),
                    Updates.set("streak", streak)
            ));

        }
        issuer.embed(builder.build());
    }
}
