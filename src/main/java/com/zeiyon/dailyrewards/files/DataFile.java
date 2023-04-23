package com.zeiyon.dailyrewards.files;

import com.zeiyon.dailyrewards.managers.CooldownsManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class DataFile {

    private static File file;
    private static FileConfiguration fileConfig;

    //Generates the data.yml file (or finds it)
    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("Dailyrewards").getDataFolder(), "data.yml");

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

    public static void loadData() {
        //load data file
        List<String> s = fileConfig.getStringList("cooldowns");
        for (String str : s) {
            String[] words = str.split(":");
            CooldownsManager.getDailyCooldown().asMap().put(UUID.fromString(words[0]), Long.parseLong(words[1]));
        }
        file.delete();
    }

    public static void saveData() {
        List<String> s = fileConfig.getStringList("cooldowns");
        s.clear();
        DataFile.getFileConfig().set("cooldowns", s);
        for (Object u : CooldownsManager.getDailyCooldown().asMap().keySet()) {
            s.add(u.toString() + ":" + CooldownsManager.getDailyCooldown().asMap().get(u));
        }

        fileConfig.set("cooldowns", s);
        DataFile.save();
    }

    //Getter for the Bukkit data.yml config
    public static FileConfiguration getFileConfig() {
        return fileConfig;
    }

    public static void save() {
        try {
            fileConfig.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
