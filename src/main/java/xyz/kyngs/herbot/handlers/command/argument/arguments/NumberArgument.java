package xyz.kyngs.herbot.handlers.command.argument.arguments;

import xyz.kyngs.herbot.handlers.command.argument.AbstractArgument;
import xyz.kyngs.herbot.handlers.command.argument.NumberState;

public abstract class NumberArgument extends AbstractArgument {

    protected final NumberState numberState;

    public NumberArgument(NumberState numberState, String... optionsOrDescription) {
        super("číslo", optionsOrDescription);
        this.numberState = numberState;
    }

    public NumberArgument(String... optionsOrDescription) {
        super("číslo", optionsOrDescription);
        numberState = NumberState.ALL;
    }

    @Override
    public String generateDescription() {
        return numberState.getName() + " " + super.generateDescription();
    }
}
