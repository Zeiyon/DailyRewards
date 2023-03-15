package com.zeiyon.dailyrewards.managers;

import com.zeiyon.dailyrewards.Main;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class RewardsManager {
    //Check if cumulative rewards is enabled or disabled.
    public static Boolean cumulative = Main.getPlugin().getConfig().getBoolean("Cumulative_Rewards");

    public static final HashMap<UUID, Integer> dailyStreak = new HashMap<>();

    public static final HashMap<UUID, Integer> weeklyStreak = new HashMap<>();

    public static void claimDailyReward(Player p) {
        if (!CooldownsManager.getDailyCooldown().asMap().containsKey(p.getUniqueId())) {
            p.sendMessage("Claiming reward!");
            if (cumulative) {
                //cumulative rewards are enabled.
                if (dailyStreak.containsKey(p.getUniqueId())) {
                    if (dailyStreak.get(p.getUniqueId()) >= Main.getPlugin().rewardsLoader.getDailyItemsArray().size() - 1) {
                        dailyStreak.put(p.getUniqueId(), 0);
                        p.getInventory().addItem(Main.getPlugin().rewardsLoader.getDailyItemsArray().get(0));
                    } else {
                        dailyStreak.put(p.getUniqueId(), dailyStreak.get(p.getUniqueId()) + 1);
                        p.getInventory().addItem(Main.getPlugin().rewardsLoader.getDailyItemsArray().get(dailyStreak.get(p.getUniqueId())));
                    }
                } else {
                    dailyStreak.put(p.getUniqueId(), 0);
                    p.getInventory().addItem(Main.getPlugin().rewardsLoader.getDailyItemsArray().get(0));
                }
            } else {
                //cumulative rewards is disabled
                //Gets random int between 0 and the max amount of rewards and gives the player the reward corresponding with that item.
                int rand = (int) (Math.random() * Main.getPlugin().rewardsLoader.getDailyItemsArray().size());
                p.getInventory().addItem(Main.getPlugin().rewardsLoader.getDailyItemsArray().get(rand));
            }
            CooldownsManager.getDailyCooldown().put(p.getUniqueId(), System.currentTimeMillis() + CooldownsManager.amountDaily);
        } else {
            //Cooldown is not up yet.
            p.sendMessage("Must wait " + CooldownsManager.dailyHoursRemaining(p) + " hours & " + CooldownsManager.dailyMinutesRemaining(p) + " minutes");
        }
    }

    public static Integer getStreak(Player p) {
        return dailyStreak.get(p.getUniqueId());
    }
    public static Boolean ifContains(Player p) {
        return dailyStreak.containsKey(p.getUniqueId());
    }
}
