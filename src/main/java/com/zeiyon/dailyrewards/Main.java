package com.zeiyon.dailyrewards;

import com.zeiyon.dailyrewards.files.DataFile;
import com.zeiyon.dailyrewards.files.MessagesFile;
import com.zeiyon.dailyrewards.files.RewardsFile;
import com.zeiyon.dailyrewards.files.RewardsLoader;
import com.zeiyon.dailyrewards.managers.CommandsManager;
import com.zeiyon.dailyrewards.menus.MenuListener;
import com.zeiyon.dailyrewards.menus.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Main extends JavaPlugin {

    private static Main plugin;
    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
    public RewardsLoader rewardsLoader;

    @Override
    public void onEnable() {

        //Method for loading all the files
        loadFiles();

        rewardsLoader = new RewardsLoader();
        plugin = this;

        getCommand("dailyrewards").setExecutor(new CommandsManager());
        Bukkit.getServer().getPluginManager().registerEvents(new MenuListener(), this);
    }

    @Override
    public void onDisable() {
        DataFile.saveData();
    }

    private void loadFiles() {
        //Config Setup
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //Rewards.yml setup
        RewardsFile.setup();
        RewardsFile.getFileConfig().options().copyDefaults();
        RewardsFile.save();

        //Data.yml setup
        DataFile.setup();
        DataFile.getFileConfig().options().copyDefaults();
        DataFile.loadData();

        //Messages.yml setup
        MessagesFile.setup();
        MessagesFile.getFileConfig().options().copyDefaults();
        MessagesFile.save();
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
}