package com.zeiyon.dailyrewards.menus;

import com.zeiyon.dailyrewards.Main;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

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
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        return;
    }

    @Override
    public void setMenuItems() {

        ArrayList<ItemStack> itemsArray = Main.getPlugin().test.getItemsArray();

        for (ItemStack item : Main.getPlugin().test.getItemsArray()) {
            inventory.addItem(item);
            System.out.println("Test");
        }

    }
}
