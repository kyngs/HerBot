package xyz.kyngs.herbot.handlers.command.argument.arguments;

import net.dv8tion.jda.api.JDA;
import xyz.kyngs.herbot.handlers.command.argument.AbstractArgument;

public class OptionArgument extends AbstractArgument {
    public OptionArgument(String... optionsOrDescription) {
        super("příkaz", optionsOrDescription);
    }

    @Override
    public Object parseArg(String arg, JDA jda) {
        for (String s : optionsOrDescription) {
            if (s.contentEquals(arg)) {
                return arg;
            }
        }
        throw new IllegalArgumentException();
    }
}
