package xyz.kyngs.herbot.handlers.user;

import java.time.LocalDate;
import java.util.List;

public class UserProfile {

    private final List<String> perms;
    private long coins;
    private long xp;
    private int dailyStreak;
    private LocalDate lastDailyClaim;

    public UserProfile(long coins, long xp, int dailyStreak, LocalDate lastDailyClaim, List<String> perms) {
        this.coins = coins;
        this.xp = xp;
        this.dailyStreak = dailyStreak;
        this.lastDailyClaim = lastDailyClaim;
        this.perms = perms;
    }

    public boolean hasPermission(String perm) {
        return perm.contentEquals("") || perms.contains("*") || perms.contains(perm);
    }

    public long getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }

    public long getXp() {
        return xp;
    }

    public void setXp(long xp) {
        this.xp = xp;
    }

    public int getDailyStreak() {
        return dailyStreak;
    }

    public void setDailyStreak(int dailyStreak) {
        this.dailyStreak = dailyStreak;
    }

    public LocalDate getLastDailyClaim() {
        return lastDailyClaim;
    }

    public void setLastDailyClaim(LocalDate lastDailyClaim) {
        this.lastDailyClaim = lastDailyClaim;
    }

    public List<String> getPerms() {
        return perms;
    }

    public void allowPermission(String perm) {
        if (!perms.contains(perm)) perms.add(perm);
    }

    public void declinePermission(String perm) {
        perms.remove(perm);
    }

    public void addCoins(int toAdd) {
        coins += toAdd;
    }

    public void removeCoins(int toRemove) {
        coins -= toRemove;
    }
}
