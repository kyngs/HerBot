package xyz.kyngs.herbot.bot.eco;

import co.aikar.commands.JDACommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.dv8tion.jda.api.entities.Member;
import xyz.kyngs.herbot.bot.command.EmbedHelper;

@CommandAlias("balance|bal|money")
@Description("Zobrazí tvůj aktuální zůstatek")
public class BalanceCommand extends EconomyCommand {
    public BalanceCommand(EconomyDaemon parent) {
        super(parent);
    }

    @Default
    public void balance(Member member, JDACommandIssuer issuer) {
        var builder = EmbedHelper.GREEN.prepare(member.getUser());
        builder.setTitle("Na účtě máš " + getUser(member).getLong("balance") + " coinů");
        issuer.embed(builder.build());
    }
}
