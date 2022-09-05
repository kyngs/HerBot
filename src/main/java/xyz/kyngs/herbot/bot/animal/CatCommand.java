package xyz.kyngs.herbot.bot.animal;

import co.aikar.commands.JDACommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;

@CommandAlias("cat|kočka|kočička|meow")
@Description("Pošle náhodnou kočičku")
public class CatCommand extends AnimalCommand {
    public CatCommand(AnimalDaemon parent) {
        super(parent);
    }

    @Default
    public void cat(JDACommandIssuer issuer) {
        issuer.sendMessage(readJsonURL("https://api.thecatapi.com/api/images/get?format=json", "Nepodařilo se načíst kočičku :("));
    }
}
