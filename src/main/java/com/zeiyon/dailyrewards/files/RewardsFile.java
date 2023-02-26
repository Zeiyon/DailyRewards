package com.zeiyon.dailyrewards.files;

import com.zeiyon.dailyrewards.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

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
    }

    //Getter for the Bukkit rewards.ynl config
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
