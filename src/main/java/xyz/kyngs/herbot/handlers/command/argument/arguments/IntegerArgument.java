package xyz.kyngs.herbot.handlers.command.argument.arguments;

import net.dv8tion.jda.api.JDA;
import xyz.kyngs.herbot.handlers.command.argument.NumberState;

public class IntegerArgument extends NumberArgument {

    public IntegerArgument(String... optionsOrDescription) {
        super(optionsOrDescription);
    }

    public IntegerArgument(NumberState numberState, String... optionsOrDescription) {
        super(numberState, optionsOrDescription);
    }

    @Override
    public Object parseArg(String arg, JDA jda) {
        var num = Integer.parseInt(arg);
        switch (numberState) {
            case NEGATIVE -> {
                if (num >= 0) throw new IllegalArgumentException();
            }
            case POSITIVE -> {
                if (num <= 0) throw new IllegalArgumentException();
            }
        }
        return num;
    }
}
