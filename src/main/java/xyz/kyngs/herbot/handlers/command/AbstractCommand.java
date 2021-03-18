package xyz.kyngs.herbot.handlers.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xyz.kyngs.herbot.HerBot;
import xyz.kyngs.herbot.handlers.user.UserProfile;
import xyz.kyngs.herbot.util.Argument;
import xyz.kyngs.herbot.util.ClassUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static xyz.kyngs.herbot.util.ExceptionUtil.completedExceptionally;

public abstract class AbstractCommand implements CommandExecutor {

    protected final HerBot herBot;
    protected final String description;
    @SuppressWarnings("rawtypes")
    private final List<Argument> args;
    private final String permission;

    public AbstractCommand(HerBot herBot, String description, String permission) {
        this.herBot = herBot;
        this.description = description;
        this.permission = permission;
        args = new ArrayList<>();
    }

    public <T> void addArg(Class<T> type, String... names) {
        args.add(new Argument<T>(type, names));
    }

    @Override
    public void onCommand(User author, Guild guild, TextChannel channel, Message message, String[] args, UserProfile profile, GuildMessageReceivedEvent event) {
        if (everythingOK(message, args, profile)) return;

        exec(author, guild, channel, message, args, profile, event);

    }

    private boolean everythingOK(Message message, String[] args, UserProfile profile) {
        if (!profile.hasPermission(permission)) {
            var builder = new EmbedBuilder();
            builder.setColor(Color.RED);
            builder.setTitle("Tuto akci nemůžeš provést");
            builder.setDescription("Na tuto akci nemáš dostatečné oprávnění");
            message.reply(builder.build()).mentionRepliedUser(false).queue();
            return false;
        }
        if (args.length < this.args.size()) {
            var builder = new EmbedBuilder();
            builder.setTitle("Špatný počet argumentů");
            builder.setDescription("Chybí ti argumenty: ");
            builder.setColor(Color.RED);
            this.args.listIterator(args.length).forEachRemaining(argument -> {
                var type = argument.getType();
                String typeName = ClassUtil.classify(type).getName();

                var nameBuilder = new StringBuilder();

                for (String option : argument.getOptions()) {
                    nameBuilder.append(option);
                    nameBuilder.append("|");
                }

                nameBuilder.deleteCharAt(nameBuilder.length() - 1);

                builder.addField(nameBuilder.toString(), typeName, true);
            });
            message.reply(builder.build()).mentionRepliedUser(false).queue();
            return false;
        }

        boolean good = true;
        var builder = new EmbedBuilder();

        builder.setTitle("Špatné argumenty");
        builder.setDescription("Tyto argumenty máš špatně:");
        builder.setColor(Color.RED);

        for (int i = 0; i < this.args.size(); i++) {
            var argument = this.args.get(i);
            var arg = args[i];
            var type = ClassUtil.classify(argument.getType());

            var lookingGood = switch (type) {
                case TEXT, UNKNOWN -> true;
                case NUMBER -> !completedExceptionally(() -> Integer.parseInt(arg));
            };

            if (good) good = lookingGood;

            if (!lookingGood) {
                var nameBuilder = new StringBuilder();

                for (String option : argument.getOptions()) {
                    nameBuilder.append(option);
                    nameBuilder.append("|");
                }

                nameBuilder.deleteCharAt(nameBuilder.length() - 1);
                builder.addField(nameBuilder.toString(), String.format("Musí být %s", type.getName()), true);
            }

        }

        if (!good) {
            message.reply(builder.build()).mentionRepliedUser(false).queue();
            return false;
        }
        return true;
    }

    public abstract void exec(User author, Guild guild, TextChannel channel, Message message, String[] args, UserProfile profile, GuildMessageReceivedEvent event);

    public String getDescription() {
        return description;
    }

}
