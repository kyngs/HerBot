package xyz.kyngs.herbot.bot.animal;

import co.aikar.commands.JDACommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;

@CommandAlias("ducc|dicc|duck|kachna")
@Description("Pošle náhodnou kachnu")
public class DuccCommand extends AnimalCommand {
    public DuccCommand(AnimalDaemon parent) {
        super(parent);
    }

    @Default
    public void ducc(JDACommandIssuer issuer) {
        issuer.sendMessage(readJsonURL("https://random-d.uk/api/v2/random", "Nepodařilo se načíst kachnu :("));
    }
}
