package com.zeiyon.dailyrewards.managers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CooldownsManager {

    //Cooldowns for Daily Rewards
    private static int amountDaily = 86400000;

    private static Cache<UUID, Long> dailyCooldown = CacheBuilder.newBuilder().expireAfterWrite(amountDaily, TimeUnit.MILLISECONDS).build();


    public static Cache getDailyCooldown() {
        return dailyCooldown;
    }
    
    public static long dailyHoursRemaining(Player p) {
        return (dailyCooldown.asMap().get(p.getUniqueId()) - System.currentTimeMillis())/1000/3600;
    }
    public static long dailyMinutesRemaining(Player p) {
        return ((dailyCooldown.asMap().get(p.getUniqueId()) - System.currentTimeMillis())/1000 - CooldownsManager.dailyHoursRemaining(p)*3600)/60;
    }

    public static int getAmountDaily() {
        return amountDaily;
    }



    //Cooldowns for Weekly Rewards
    public static int amountWeekly = 604800000;

    static Cache<UUID, Long> weeklyCooldown = CacheBuilder.newBuilder().expireAfterWrite(amountWeekly, TimeUnit.MILLISECONDS).build();

    public static Cache getWeeklyCooldown() {
        return weeklyCooldown;
    }
    public static long weeklyHoursRemaining(Player p) {
        return (weeklyCooldown.asMap().get(p.getUniqueId()) - System.currentTimeMillis())/1000/3600;
    }
    public static long weeklyMinutesRemaining(Player p) {
        return ((weeklyCooldown.asMap().get(p.getUniqueId()) - System.currentTimeMillis())/1000 - CooldownsManager.weeklyHoursRemaining(p)*3600)/60;
    }

    public static int getAmountWeekly() {
        return amountWeekly;
    }
}
