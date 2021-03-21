package xyz.kyngs.herbot.handlers.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xyz.kyngs.herbot.HerBot;
import xyz.kyngs.herbot.handlers.command.argument.AbstractArgument;
import xyz.kyngs.herbot.handlers.command.argument.Arguments;
import xyz.kyngs.herbot.handlers.user.UserProfile;
import xyz.kyngs.herbot.util.ExceptionUtil;
import xyz.kyngs.herbot.util.ImmutableEntry;
import xyz.kyngs.herbot.util.embed.EmbedHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommand implements CommandExecutor {

    protected final HerBot herBot;
    protected final String description;
    private final List<AbstractArgument> args;

    public String getPermission() {
        return permission;
    }

    private final String permission;

    public AbstractCommand(HerBot herBot, String description, String permission) {
        this.herBot = herBot;
        this.description = description;
        this.permission = permission;
        args = new ArrayList<>();
    }

    public void addArg(AbstractArgument arg) {
        args.add(arg);
    }

    @Override
    public void onCommand(User author, Guild guild, TextChannel channel, Message message, String[] args, UserProfile profile, GuildMessageReceivedEvent event) {
        if (!hasPerm(message, profile, author) || !rightArgumentSIze(message, args, author) || !rightArgumentType(message, args, author))
            return;

        var list = new ArrayList<ImmutableEntry<String, AbstractArgument>>();

        for (int i = 0; i < this.args.size(); i++) {
            list.add(new ImmutableEntry<>(args[i], this.args.get(i)));
        }

        exec(author, guild, channel, message, new Arguments(list, herBot.getJda()), profile, event);

    }

    private boolean hasPerm(Message message, UserProfile profile, User author) {
        if (!profile.hasPermission(permission)) {
            var builder = EmbedHelper.RED.prepare(author);
            builder.setTitle("Tuto akci nemůžeš provést");
            builder.setDescription("Na tuto akci nemáš dostatečné oprávnění");
            message.reply(builder.build()).mentionRepliedUser(false).queue();
            return false;
        }
        return true;
    }

    private boolean rightArgumentSIze(Message message, String[] args, User author) {
        if (args.length < this.args.size()) {
            var builder = EmbedHelper.RED.prepare(author);
            builder.setTitle("Špatný počet argumentů");
            builder.setDescription("Chybí ti argumenty: ");
            this.args.listIterator(args.length).forEachRemaining(argument -> {
                builder.addField(argument.generateDescription(), argument.getName(), true);
            });
            message.reply(builder.build()).mentionRepliedUser(false).queue();
            return false;
        }
        return true;
    }

    private boolean rightArgumentType(Message message, String[] args, User author) {
        boolean good = true;
        var builder = EmbedHelper.RED.prepare(author);

        builder.setTitle("Špatné argumenty");
        builder.setDescription("Tyto argumenty máš špatně:");

        for (int i = 0; i < this.args.size(); i++) {
            var argument = this.args.get(i);
            var arg = args[i];

            if (!ExceptionUtil.completedExceptionally(() -> argument.parseArg(arg, herBot.getJda()))) continue;

            builder.addField(argument.generateDescription(), argument.getName(), true);
            good = false;

        }

        if (!good) {
            message.reply(builder.build()).mentionRepliedUser(false).queue();
            return false;
        }
        return true;
    }

    public abstract void exec(User author, Guild guild, TextChannel channel, Message message, Arguments args, UserProfile profile, GuildMessageReceivedEvent event);

    public String getDescription() {
        return description;
    }

}
