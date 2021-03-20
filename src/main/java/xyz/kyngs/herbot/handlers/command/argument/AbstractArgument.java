package xyz.kyngs.herbot.handlers.command.argument;

import net.dv8tion.jda.api.JDA;

public abstract class AbstractArgument {

    protected final String[] optionsOrDescription;
    protected final String name;

    public AbstractArgument(String name, String... optionsOrDescription) {
        this.name = name;
        this.optionsOrDescription = optionsOrDescription;
    }

    public String getName() {
        return name;
    }

    public String[] getOptionsOrDescription() {
        return optionsOrDescription;
    }

    public abstract Object parseArg(String arg, JDA jda);

    public String generateDescription() {
        if (optionsOrDescription.length == 1) {
            return optionsOrDescription[0];
        } else {
            var nameBuilder = new StringBuilder();

            for (String option : optionsOrDescription) {
                nameBuilder.append(option);
                nameBuilder.append("|");
            }

            nameBuilder.deleteCharAt(nameBuilder.length() - 1);

            return nameBuilder.toString();
        }
    }

}
