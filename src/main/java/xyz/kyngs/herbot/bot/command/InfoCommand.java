package xyz.kyngs.herbot.bot.command;

import co.aikar.commands.JDACommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.dv8tion.jda.api.entities.User;

import static xyz.kyngs.herbot.bot.HerBot.VERSION;

@CommandAlias("info|version")
@Description("Zobrazí informace o botovi")
public class InfoCommand extends Command<CommandDaemon> {
    public InfoCommand(CommandDaemon parent) {
        super(parent);
    }

    @Default
    public void info(JDACommandIssuer issuer, User author) {
        var builder = EmbedHelper.GREEN.prepare(author);

        builder.setTitle("Informace");
        builder.addField("Verze:", VERSION, false);
        builder.addField("Máš nějaký dotaz k botovi, nebo jsi našel chybu? Zkontaktuj mě.", "Discord: kyngs#0666", false);

        issuer.embed(builder.build());
    }

}
