package com.zeiyon.dailyrewards.files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessagesFile {

    private static File file;
    private static FileConfiguration fileConfig;

    //Generates the messages.yml file (or finds it)
    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("Dailyrewards").getDataFolder(), "messages.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Sets the file java object as a configurable Bukkit object
        fileConfig = YamlConfiguration.loadConfiguration(file);

        //Add two example items to the messages file
        fileConfig.options().copyDefaults(true);

        List<String> help = new ArrayList<>();
        help.add("&c------------------------[DailyRewards]------------------------");
        help.add("&7- /dr help - This menu");
        help.add("&7- /dr claim - Claims the Daily-Reward");
        help.add("&7- /dr claim weekly - Claims the Weekly-Reward");
        help.add("&7- /dr rewards - Opens the rewards menu");
        fileConfig.addDefault("help-command", help);

        List<String> adminHelp = new ArrayList<>();
        adminHelp.add("&4------------------------[ADMIN]------------------------");
        adminHelp.add("&7- /dr addTime {username} {time} - Add time onto a player's dailyreward");
        adminHelp.add("&7- /dr removeTime {username} {time} - Remove time onto a player's dailyreward");
        adminHelp.add("&7- /dr reset {username} - Reset a player's dailyreward timer");
        adminHelp.add("&7- /dr viewtime {username} - View a player's remaining dailyrewards time");
        fileConfig.addDefault("help-command-admin", adminHelp);

        fileConfig.addDefault("dr-addTime-usage", "&cusage: /dr addTime/removeTime {user} {amount}");
        fileConfig.addDefault("dr-addTime-notvalid", "&cNot a valid user");
        fileConfig.addDefault("dr-addTime-notvalidnumber", "&cPlease enter a valid number");
    }

    //Getter for the Bukkit messages.yml config
    public static FileConfiguration getFileConfig() {
        return fileConfig;
    }

    //Sets the Bukkit file equal to the Java file when called, which also functions as a reloading method
    public static void reload() {
        fileConfig = YamlConfiguration.loadConfiguration(file);
    }

    //Method for saving the messages.yml config outside the class
    public static void save() {
        try {
            fileConfig.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
