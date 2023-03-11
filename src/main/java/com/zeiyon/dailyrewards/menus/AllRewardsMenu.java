package com.zeiyon.dailyrewards.menus;

import com.zeiyon.dailyrewards.Main;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class AllRewardsMenu extends Menu{

    public AllRewardsMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "DailyRewards - All Rewards";
    }

    @Override
    public int getSlots() {
        return Integer.valueOf(Main.getPlugin().getConfig().getString("All_Rewards_Menu.Size"));
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        return;
    }

    @Override
    public void setMenuItems() {

        for (ItemStack item : Main.getPlugin().rewardsLoader.getItemsArray()) {
            inventory.addItem(item);
        }

    }
}
