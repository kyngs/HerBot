package xyz.kyngs.herbot.util;

public enum ClassType {

    NUMBER("číslo"), TEXT("text"), UNKNOWN("cokoliv");

    private final String name;

    ClassType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
