package xyz.kyngs.herbot.handlers.command.commands.info;

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

public class InfoCommand extends AbstractCommand {
    public InfoCommand(HerBot herBot, String description) {
        super(herBot, description, "");
    }

    @Override
    public void exec(User author, Guild guild, TextChannel channel, Message message, Arguments args, UserProfile profile, GuildMessageReceivedEvent event) {
        var builder = EmbedHelper.GREEN.prepare(author);

        builder.setTitle("Informace");
        builder.addField("Verze:", "1.3", false);
        builder.addField("Sestavení:", "5", false);
        builder.addField("Máš nějaký dotaz k botovi, nebo jsi našel chybu? Zkontaktuj mě.", "Discord: kyngs#0666 Twitch: kyngskyngs", false);

        message.reply(builder.build()).mentionRepliedUser(false).queue();

    }
}
