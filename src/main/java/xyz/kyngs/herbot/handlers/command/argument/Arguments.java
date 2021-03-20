package xyz.kyngs.herbot.handlers.command.argument;

import net.dv8tion.jda.api.JDA;
import xyz.kyngs.herbot.util.ImmutableEntry;

import java.util.List;

public class Arguments {

    private final List<ImmutableEntry<String, AbstractArgument>> arguments;
    private final JDA jda;

    public Arguments(List<ImmutableEntry<String, AbstractArgument>> arguments, JDA jda) {
        this.arguments = arguments;
        this.jda = jda;
    }

    public <T> T getArgument(int pos) {
        var entry = arguments.get(pos);
        //noinspection unchecked
        return (T) entry.getValue().parseArg(entry.getKey(), jda);
    }

}
