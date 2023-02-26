package com.zeiyon.dailyrewards.files;

import com.zeiyon.dailyrewards.Main;
import com.zeiyon.dailyrewards.menus.DailyRewardsMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class RewardsLoader {

    private final HashMap<String, ItemStack> customItems;
    private final Main main;

    ArrayList<ItemStack> itemsArray = new ArrayList<>();

    public RewardsLoader(Main main) {
        this.main = main;
        customItems = new HashMap<>();
        loadItemStacks();
    }

    private void loadItemStacks(){
        //Add all the ItemStacks in the Config and the custom file to the HashMap
        //'final' means that the value of the Variable won't and can't change

        for(String itemStackName : RewardsFile.get().getConfigurationSection("").getKeys(false)){
            customItems.put(itemStackName, RewardsFile.get().getItemStack(itemStackName));
        }
        for(String itemStackFromConfigName : RewardsFile.get().getConfigurationSection("items").getKeys(false)){
            final ItemStack itemStack = new ItemStack(Material.AIR);
            final FileConfiguration config = RewardsFile.get();
            final String itemPath = "items."+itemStackFromConfigName;
            if(config.getString(itemPath+".material") != null) {
                itemStack.setType(Material.valueOf(config.getString(itemPath+ ".material")));
            }
            if(config.getInt(itemPath+".amount") != 0 && config.getInt(itemPath+".amount") < 65){
                itemStack.setAmount(RewardsFile.get().getInt(itemPath+".amount"));
            }
            if(config.getConfigurationSection(itemPath+".meta") != null) {
                final ItemMeta itemMeta = itemStack.getItemMeta();
                if(config.getString(itemPath+".meta.displayname") != null) {
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString(itemPath+".meta.displayname")));
                }
            }
            customItems.put(itemStackFromConfigName, itemStack);
            System.out.println();
            System.out.println(itemStackFromConfigName);
            itemsArray.add(itemStack);
        }
    }

    public ItemStack getItem(String name){
        //Get the ItemStack with the name, if it doesn't find anything, it returns null because of the HashMap
        return customItems.get(name);
    }

    public ArrayList<ItemStack> getItemsArray() {
        return itemsArray;
    }
}
