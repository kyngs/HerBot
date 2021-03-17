package xyz.kyngs.herbot.handlers.command.commands.economy;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xyz.kyngs.herbot.HerBot;
import xyz.kyngs.herbot.handlers.command.AbstractCommand;
import xyz.kyngs.herbot.handlers.user.UserProfile;
import xyz.kyngs.herbot.util.MemberUtil;

import java.awt.*;

public class PayCommand extends AbstractCommand {

    public PayCommand(HerBot herBot, String description) {
        super(herBot, description, "");
        addArg(String.class, "uživatel");
        addArg(Integer.class, "částka");
    }

    @Override
    public void exec(User author, Guild guild, TextChannel channel, Message message, String[] args, UserProfile profile, GuildMessageReceivedEvent event) {
        var user = MemberUtil.getUser(args[0], herBot.getJda());
        if (user == null) {
            message.reply("Uživatel nenalezen!").mentionRepliedUser(false).queue();
            return;
        }
        var amount = Integer.parseInt(args[1]);

        if (amount < 0) {
            var builder = new EmbedBuilder();
            builder.setTitle("Hodnota nemůže být negativní");
            builder.setColor(Color.RED);
            message.reply(builder.build()).mentionRepliedUser(false).queue();
            return;
        }

        if (amount > profile.getCoins()) {
            var builder = new EmbedBuilder();
            builder.setTitle("Na tuto akci nemáš dostatek prostředků");
            builder.setColor(Color.RED);
            message.reply(builder.build()).mentionRepliedUser(false).queue();
            return;
        }

        var targetProfile = herBot.getUserHandler().getUser(user.getId());
        profile.removeCoins(amount);
        targetProfile.addCoins(amount);

        var builder = new EmbedBuilder();

        builder.setColor(Color.GREEN);
        builder.setTitle("Úspěch");
        builder.setDescription("Coiny odeslány");

        message.reply(builder.build()).mentionRepliedUser(false).queue();
    }
}
