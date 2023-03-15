package com.zeiyon.dailyrewards.files;

import com.zeiyon.dailyrewards.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RewardsFile {

    private static File file;
    private static FileConfiguration fileConfig;

    //Generates the rewards.yml file (or finds it)
    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("Dailyrewards").getDataFolder(), "rewards.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Sets the file java object as a configurable Bukkit object
        fileConfig = YamlConfiguration.loadConfiguration(file);

        //Add two example items to the rewards file
        fileConfig.options().copyDefaults(true);
        fileConfig.addDefault("items.ItemName.material", "DIRT");
        fileConfig.addDefault("items.ItemName.amount", 1);
        fileConfig.addDefault("items.ItemName.meta.displayname", "&e&lDailyRewards Example Item");
        List<String> lore = new ArrayList<>();
        lore.add("&7Default Lore");
        lore.add("&7Default Lore part 2");
        fileConfig.addDefault("items.ItemName.meta.lore", lore);
        fileConfig.addDefault("items.ItemName.meta.unbreakable", false);
        fileConfig.addDefault("items.ItemName.weekly", false);

        fileConfig.addDefault("items.ItemName2.material", "GRASS");
        fileConfig.addDefault("items.ItemName2.amount", 1);
        fileConfig.addDefault("items.ItemName2.meta.displayname", "&b&lDailyRewards Example Item 2");
        List<String> lore2 = new ArrayList<>();
        lore2.add("&7Another Example Lore");
        fileConfig.addDefault("items.ItemName2.meta.lore", lore2);
        fileConfig.addDefault("items.ItemName2.meta.unbreakable", false);
        fileConfig.addDefault("items.ItemName2.weekly", true);
    }

    //Getter for the Bukkit rewards.yml config
    public static FileConfiguration get() {
        return fileConfig;
    }

    //Sets the Bukkit file equal to the Java file when called, which also functions as a reloading method
    public static void reload() {
        fileConfig = YamlConfiguration.loadConfiguration(file);
    }

    //Method for saving the rewards.yml config outside the class
    public static void save() {
        try {
            fileConfig.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
