package com.zeiyon.dailyrewards.menus;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zeiyon.dailyrewards.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MainMenu extends Menu {

    void test() {
        System.out.println("test");
    }

    public MainMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("Main_Menu_Title"));
    }

    @Override
    public int getSlots() {
        return Main.getPlugin().getConfig().getInt("Main_Menu_Size");
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        //If clicked on All_Rewards_Item
        if (e.getCurrentItem().getType() != null && e.getCurrentItem().getType() != Material.AIR) {
            if (e.getCurrentItem().getType() == Material.matchMaterial(Main.getPlugin().getConfig().getString("All_Rewards_Item.Material"))) {
                AllRewardsMenu menu = new AllRewardsMenu(Main.getPlayerMenUtility(p));
                menu.open();
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("Daily_Item.On-Click-Message")));



                //If clicked on Daily_Item
            } else if (e.getCurrentItem().getType() == Material.matchMaterial(Main.getPlugin().getConfig().getString("Daily_Item.Material"))) {

                if (!Main.getCooldown().asMap().containsKey(p.getUniqueId())) {
                    p.sendMessage("Claiming Daily Reward");
                    //Gets random int between 0 and the max amount of rewards and gives the player the reward corresponding with that item.
                    int rand = (int) (Math.random() * Main.getPlugin().rewardsLoader.getItemsArray().size());
                    p.getInventory().addItem(Main.getPlugin().rewardsLoader.getItemsArray().get(rand));
                    Main.getCooldown().put(p.getUniqueId(), System.currentTimeMillis() + 20000);
                } else {
                    long remaining = (long)Main.getCooldown().asMap().get(p.getUniqueId()) - System.currentTimeMillis();
                    p.sendMessage("Must wait " + (int)(remaining/1000) + " Hours");

                }
                p.closeInventory();


                //If clicked on Weekly_Item
            } else if (e.getCurrentItem().getType() == Material.matchMaterial(Main.getPlugin().getConfig().getString("Weekly_Item.Material"))) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("Weekly_Item.On-Click-Message")));
                p.closeInventory();
            }
        }

    }

    @Override
    public void setMenuItems() {

        //All Rewards Item
        ItemStack rewards = new ItemStack(Material.matchMaterial(Main.getPlugin().getConfig().getString("All_Rewards_Item.Material")));
        ItemMeta rewardsMeta = rewards.getItemMeta();
        rewardsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("All_Rewards_Item.Name")));
        ArrayList<String> rewardsLore = new ArrayList<>();
        //Iterates over the lore for Item in config and adds it to the lore ArrayList
        for (int i = 0; i < Main.getPlugin().getConfig().getStringList("All_Rewards_Item.Lore").size(); i++) {
            rewardsLore.add(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getStringList("All_Rewards_Item.Lore").get(i)));
        }
        rewardsMeta.setLore(rewardsLore);
        rewards.setItemMeta(rewardsMeta);
        inventory.setItem(Main.getPlugin().getConfig().getInt("All_Rewards_Item.Slot"), rewards);


        //Daily-Reward Item
        ItemStack daily = new ItemStack(Material.matchMaterial(Main.getPlugin().getConfig().getString("Daily_Item.Material")));
        ItemMeta dailyMeta = daily.getItemMeta();
        dailyMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("Daily_Item.Name")));
        ArrayList<String> dailyLore = new ArrayList<>();
        //Iterates over the lore for Item in config and adds it to the lore ArrayList
        for (int i = 0; i < Main.getPlugin().getConfig().getStringList("Daily_Item.Lore").size(); i++) {
            dailyLore.add(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getStringList("Daily_Item.Lore").get(i)));
        }
        dailyMeta.setLore(dailyLore);
        daily.setItemMeta(dailyMeta);
        inventory.setItem(Main.getPlugin().getConfig().getInt("Daily_Item.Slot"), daily);


        //Weekly-Reward Item
        ItemStack weekly = new ItemStack(Material.matchMaterial(Main.getPlugin().getConfig().getString("Weekly_Item.Material")));
        ItemMeta weeklyMeta = weekly.getItemMeta();
        weeklyMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("Weekly_Item.Name")));
        ArrayList<String> weeklyLore = new ArrayList<>();
        //Iterates over the lore for Item in config and adds it to the lore ArrayList
        for (int i = 0; i < Main.getPlugin().getConfig().getStringList("Weekly_Item.Lore").size(); i++) {
            weeklyLore.add(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getStringList("Weekly_Item.Lore").get(i)));
        }
        weeklyMeta.setLore(weeklyLore);
        weekly.setItemMeta(weeklyMeta);
        inventory.setItem((Main.getPlugin().getConfig().getInt("Weekly_Item.Slot")), weekly);
    }
}
