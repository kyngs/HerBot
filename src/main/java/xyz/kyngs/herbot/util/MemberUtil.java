package xyz.kyngs.herbot.util;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;

public class MemberUtil {

    public static User getUser(String name, JDA jda) {
        try {
            User byID = jda.getUserById(name);
            if (byID != null) return byID;
        } catch (Exception ignored) {
        }
        try {
            User byTag = jda.getUserByTag(name);
            if (byTag != null) return byTag;
        } catch (Exception ignored) {
        }

        var list = jda.getUsersByName(name, false);

        if (list.size() == 1) return list.get(0);

        return null;

    }

}
