package com.zeiyon.dailyrewards.files;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RewardsLoader {

    private final HashMap<String, ItemStack> dailyItemsMap;

    private final ArrayList<ItemStack> dailyItemsArray = new ArrayList<>();
    private final ArrayList<ItemStack> weeklyItemsArray = new ArrayList<>();

    public RewardsLoader() {
        dailyItemsMap = new HashMap<>();
        loadItemStacks();
    }

    private void loadItemStacks(){

        //Add all the ItemStacks in the Config and the custom file to the HashMap
        for (String itemStackName : RewardsFile.getFileConfig().getConfigurationSection("").getKeys(false)){
            dailyItemsMap.put(itemStackName, RewardsFile.getFileConfig().getItemStack(itemStackName));
        }
        for (String itemStackFromConfigName : RewardsFile.getFileConfig().getConfigurationSection("items").getKeys(false)){
            final ItemStack itemStack = new ItemStack(Material.AIR);
            final FileConfiguration config = RewardsFile.getFileConfig();
            final String itemPath = "items."+itemStackFromConfigName;
            if(config.getString(itemPath+".material") != null) {
                itemStack.setType(Material.valueOf(config.getString(itemPath+ ".material")));
            }
            if (config.getInt(itemPath+".amount") != 0 && config.getInt(itemPath+".amount") < 65){
                itemStack.setAmount(RewardsFile.getFileConfig().getInt(itemPath+".amount"));
            }
            if(config.getConfigurationSection(itemPath+".meta") != null) {
                final ItemMeta itemMeta = itemStack.getItemMeta();
                if(config.getString(itemPath+".meta.displayname") != null) {
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString(itemPath+".meta.displayname")));
                }
                final List<String> loreList = new ArrayList<>();
                for(String lore : config.getStringList(itemPath+".meta.lore")){
                    loreList.add(ChatColor.translateAlternateColorCodes('&', lore));
                }
                loreList.add("");
                itemMeta.setLore(loreList);
                itemMeta.setUnbreakable(config.getBoolean(itemPath+".meta.unbreakable"));
                itemStack.setItemMeta(itemMeta);
            }
            dailyItemsMap.put(itemStackFromConfigName, itemStack);

            if (config.getBoolean(itemPath + ".weekly")) {
                weeklyItemsArray.add(itemStack);
            } else dailyItemsArray.add(itemStack);
        }
    }

    public HashMap<String, ItemStack> getDailyItemsMap() {
        return dailyItemsMap;
    }

    public ArrayList<ItemStack> getDailyItemsArray() {
        return dailyItemsArray;
    }

    public ArrayList<ItemStack> getWeeklyItemsArray() {
        return weeklyItemsArray;
    }
}
