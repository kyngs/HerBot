package xyz.kyngs.herbot.handlers.command.argument.arguments;

import net.dv8tion.jda.api.JDA;
import xyz.kyngs.herbot.handlers.command.argument.AbstractArgument;
import xyz.kyngs.herbot.util.MemberUtil;

public class UserArgument extends AbstractArgument {
    public UserArgument(String... optionsOrDescription) {
        super("uživatel", optionsOrDescription);
    }

    @Override
    public Object parseArg(String arg, JDA jda) {
        var user = MemberUtil.getUser(arg, jda);
        if (user == null) throw new IllegalArgumentException();
        return user;
    }
}
