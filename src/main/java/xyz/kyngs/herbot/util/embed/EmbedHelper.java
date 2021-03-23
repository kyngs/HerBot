package xyz.kyngs.herbot.util.embed;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.function.Consumer;

public enum EmbedHelper {

    GREEN(embedBuilder -> {
        embedBuilder.setColor(Color.GREEN);
    }),

    RED(embedBuilder -> {
        embedBuilder.setColor(Color.RED);
    }),

    ERROR(embedBuilder -> {
        RED.consumer.accept(embedBuilder);
        embedBuilder.setTitle("Chyba!");
    }),

    CANNOT_PERFORM(embedBuilder -> {
        RED.consumer.accept(embedBuilder);
        embedBuilder.setTitle("Tuto akci nemůžeš provést");
    }),

    SUCCESS(embedBuilder -> {
        GREEN.consumer.accept(embedBuilder);
        embedBuilder.setTitle("Úspěch");
    }),

    TOO_POOR(embedBuilder -> {
        RED.consumer.accept(embedBuilder);
        embedBuilder.setTitle("Na tuto akci nemáš dostatek prostředků");
    });

    private final Consumer<EmbedBuilder> consumer;

    EmbedHelper(Consumer<EmbedBuilder> consumer) {
        this.consumer = consumer;
    }

    public EmbedBuilder prepare(User user) {
        if (user == null) return prepare();
        var embedBuilder = new EmbedBuilder();
        embedBuilder.setFooter(String.format("Použil %s", user.getName()), user.getEffectiveAvatarUrl());
        consumer.accept(embedBuilder);
        return embedBuilder;
    }

    public EmbedBuilder prepare() {
        var embedBuilder = new EmbedBuilder();
        consumer.accept(embedBuilder);
        return embedBuilder;
    }

}
