package xyz.kyngs.herbot.handlers.command.argument;

public enum NumberState {

    POSITIVE("kladná"), ALL(""), NEGATIVE("negativní");

    private final String name;

    NumberState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
