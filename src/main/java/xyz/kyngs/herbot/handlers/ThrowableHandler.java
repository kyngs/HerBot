package xyz.kyngs.herbot.handlers;

import net.dv8tion.jda.api.EmbedBuilder;
import xyz.kyngs.herbot.HerBot;

import java.awt.*;

public class ThrowableHandler extends AbstractHandler {

    private final String channelID;

    public ThrowableHandler(HerBot herBot) {
        super(herBot);
        channelID = herBot.getConfiguration().getString("error_channel");
    }

    public void reportThrowable(Throwable throwable) {
        var builder = new EmbedBuilder();
        builder.setTitle("Chyba!");
        builder.setColor(Color.RED);

        builder.setDescription(new StringBuilder()
                .append("Při běhu bota nastala neočekávaná chyba!\n Chyba: ")
                .append(throwable.getClass().getName())
                .append(": ")
                .append(throwable.getMessage())
                .append("\n\n Stacktrace:")
        );

        for (StackTraceElement stackTraceElement : throwable.getStackTrace()) {
            builder.addField(stackTraceElement.getClassName() + "#" + stackTraceElement.getMethodName(), "Řádek: %s".formatted(String.valueOf(stackTraceElement.getLineNumber())), false);
        }

        herBot.getJda().getTextChannelById(channelID).sendMessage(builder.build()).queue();

    }

}
