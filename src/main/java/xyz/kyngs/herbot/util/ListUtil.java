package xyz.kyngs.herbot.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.join;

public class ListUtil {

    public static String serializeList(List<? extends CharSequence> list) {
        return join(",", list);
    }

    public static List<String> deserializeList(String string) {
        return new ArrayList<>(Arrays.asList(string.split(",")));
    }

}
