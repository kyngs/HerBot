package xyz.kyngs.herbot.util;

public class Argument<T> {

    private final Class<T> type;
    private final String[] options;

    public Argument(Class<T> type, String... options) {
        this.type = type;
        this.options = options;
    }

    public Class<T> getType() {
        return type;
    }

    public String[] getOptions() {
        return options;
    }
}
