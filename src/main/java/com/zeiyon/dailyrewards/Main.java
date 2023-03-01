package com.zeiyon.dailyrewards;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zeiyon.dailyrewards.files.RewardsFile;
import com.zeiyon.dailyrewards.files.RewardsLoader;
import com.zeiyon.dailyrewards.menus.MenuListener;
import com.zeiyon.dailyrewards.menus.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class Main extends JavaPlugin {

    private static Main plugin;
    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<Player, PlayerMenuUtility>();
    public RewardsLoader rewardsLoader;

    static Cache<UUID, Long> dailyCooldown = CacheBuilder.newBuilder().expireAfterWrite(20, TimeUnit.SECONDS).build();

    @Override
    public void onEnable() {

        //Config Setup onEnable
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //Rewards.yml setup onEnable
        RewardsFile.setup();
        RewardsFile.get().options().copyDefaults();
        RewardsFile.save();

        rewardsLoader = new RewardsLoader(this);

        plugin = this;

        getCommand("dailyrewards").setExecutor(new Commands());
        getCommand("test").setExecutor(new TestCommand());
        Bukkit.getServer().getPluginManager().registerEvents(new MenuListener(), this);

        System.out.println("Zeiyon's DailyRewards Plugin Enabled");
    }

    public static PlayerMenuUtility getPlayerMenUtility(Player p) {
        PlayerMenuUtility playerMenuUtility;

        if (playerMenuUtilityMap.containsKey(p)) {
            return playerMenuUtilityMap.get(p);
        } else {
            playerMenuUtility = new PlayerMenuUtility(p);
            playerMenuUtilityMap.put(p, playerMenuUtility);
            return playerMenuUtility;
        }
    }

    public static Main getPlugin() {
        return plugin;
    }

    public static Cache getCooldown() {
        return dailyCooldown;
    }
}