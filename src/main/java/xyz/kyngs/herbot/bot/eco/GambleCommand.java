package xyz.kyngs.herbot.bot.eco;

import co.aikar.commands.JDACommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Name;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import xyz.kyngs.herbot.bot.command.EmbedHelper;

import java.util.concurrent.ThreadLocalRandom;

@CommandAlias("gamble|sazka")
public class GambleCommand extends EconomyCommand {
    public GambleCommand(EconomyDaemon parent) {
        super(parent);
    }

    @Default
    public void gamble(JDACommandIssuer issuer, Member member, @Name("částka") @Description("Částka k sázce") int amount) {
        var random = ThreadLocalRandom.current();
        var profile = getUser(member);

        if (amount > profile.getLong("balance")) {
            var builder = EmbedHelper.TOO_POOR.prepare(member.getUser());
            issuer.embed(builder.build());
            return;
        }

        boolean win = random.nextInt(100) > 50;

        EmbedBuilder builder;
        if (win) {
            builder = EmbedHelper.GREEN.prepare(member.getUser());
            builder.setTitle("Výhra");
            builder.setDescription(String.format("Na účet ti bylo přidáno %s coinů", amount));
        } else {
            builder = EmbedHelper.RED.prepare(member.getUser());
            builder.setTitle("Prohra");
            builder.setDescription(String.format("Z účtu ti bylo odebráno %s coinů", amount));
        }

        getCollection().updateOne(Filters.eq("_id", member.getIdLong()), Updates.inc("balance", win ? amount : -amount));
        issuer.embed(builder.build());
    }

}
