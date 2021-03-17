package xyz.kyngs.herbot.util;

public class ExceptionUtil {

    public static boolean completedExceptionally(Runnable action) {
        try {
            action.run();
            return false;
        } catch (Exception e) {
            return true;
        }
    }

}
