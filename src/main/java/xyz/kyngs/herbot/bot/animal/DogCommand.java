package xyz.kyngs.herbot.bot.animal;

import co.aikar.commands.JDACommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;

@CommandAlias("dog|haf|pes|pejsek")
@Description("Pošle náhodného psa")
public class DogCommand extends AnimalCommand {
    public DogCommand(AnimalDaemon parent) {
        super(parent);
    }

    @Default
    public void dog(JDACommandIssuer issuer) {
        issuer.sendMessage(readJsonURL("https://random.dog/woof.json", "Nepodařilo se načíst psa :("));
    }
}
