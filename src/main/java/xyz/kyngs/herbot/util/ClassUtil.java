package xyz.kyngs.herbot.util;

import static xyz.kyngs.herbot.util.ClassType.*;

public class ClassUtil {

    public static <T> ClassType classify(Class source) {
        if (classify(source, String.class)) {
            return TEXT;
        } else if (classify(source, Integer.class) || classify(source, Long.class)) {
            return NUMBER;
        }
        return UNKNOWN;
    }

    private static <T> boolean classify(Class source, Class<T> target) {
        return source.equals(target);
    }

}
