package xyz.kyngs.herbot.handlers.command.argument.arguments;

import net.dv8tion.jda.api.JDA;
import xyz.kyngs.herbot.handlers.command.argument.AbstractArgument;

public class StringArgument extends AbstractArgument {

    public StringArgument(String... optionsOrDescription) {
        super("text", optionsOrDescription);
    }

    @Override
    public Object parseArg(String arg, JDA jda) {
        return arg;
    }
}
