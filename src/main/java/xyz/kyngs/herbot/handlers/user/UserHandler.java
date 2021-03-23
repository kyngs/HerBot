package xyz.kyngs.herbot.handlers.user;

import xyz.kyngs.herbot.HerBot;
import xyz.kyngs.herbot.handlers.AbstractHandler;
import xyz.kyngs.herbot.util.ListUtil;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class UserHandler extends AbstractHandler {

    private final Map<String, UserProfile> cache;

    public UserHandler(HerBot herBot) {
        super(herBot);
        cache = new HashMap<>();
    }

    public UserProfile getUser(String userID) {
        return cache.computeIfAbsent(userID, k -> {
            var databaseManager = herBot.getDatabaseManager();
            var future = new CompletableFuture<UserProfile>();
            databaseManager.getMySQL().sync().schedule(connection -> {
                var ps = connection.prepareStatement("SELECT coins, xp, daily_streak, last_daily_claim, permissions FROM user_data WHERE user_id=?");
                ps.setString(1, userID);
                var rs = ps.executeQuery();

                if (!rs.next()) {
                    var user = new UserProfile(5000, 0, -1, LocalDate.now().minusDays(1), new ArrayList<>());
                    saveUser(user, userID);
                    future.complete(user);
                    return;
                }

                var user = new UserProfile(rs.getLong("coins"), rs.getLong("xp"), rs.getInt("daily_streak"), rs.getDate("last_daily_claim").toLocalDate(), ListUtil.deserializeList(rs.getString("permissions")));

                future.complete(user);

            });
            return future.getNow(null);
        });
    }

    public void saveUser(UserProfile userProfile, String userID) {
        herBot.getDatabaseManager().getMySQL().sync().schedule(connection -> {
            var ps = connection.prepareStatement("REPLACE user_data(coins, user_id, xp, daily_streak, last_daily_claim, permissions) VALUES (?, ?, ?, ?, ?, ?) ");

            ps.setLong(1, userProfile.getCoins());
            ps.setString(2, userID);
            ps.setLong(3, userProfile.getXp());
            ps.setInt(4, userProfile.getDailyStreak());
            ps.setDate(5, Date.valueOf(userProfile.getLastDailyClaim()));
            ps.setString(6, ListUtil.serializeList(userProfile.getPerms()));

            ps.execute();

        });
    }

}
