package com.zeiyon.dailyrewards.menus;

import com.zeiyon.dailyrewards.Main;
import com.zeiyon.dailyrewards.managers.RewardsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RewardsMenu extends Menu{

    public RewardsMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "DailyRewards - All Rewards";
    }

    @Override
    public int getSlots() {
        return Integer.parseInt(Main.getPlugin().getConfig().getString("All_Rewards_Menu.Size"));
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        return;
    }

    @Override
    public void setMenuItems() {
        Player p = PlayerMenuUtility.getOwner();
        int index = -1;

        for (ItemStack item : Main.getPlugin().rewardsLoader.getDailyItemsArray()) {

            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();

            //Removes the extra empty line of lore added by RewardsLoader for each item
            //Every time after the first time, it removes the last 'streak' lore
            if (lore.size() > 0) {
                lore.remove(lore.size()-1);
            }

            //If the menu is set to cumulative, then it will tell you claimed reward, next rewards & upcoming rewards.
            if (RewardsManager.isCumulative() == true) {
                    if (!RewardsManager.ifContains(p)) {
                        lore.add("Upcoming Reward");
                    } else if (RewardsManager.getStreak(p) > index) {
                        lore.add("Claimed Already");
                    } else if (RewardsManager.getStreak(p) == index) {
                        lore.add("Next Reward");
                    } else if (RewardsManager.getStreak(p) < index) {
                        lore.add("Upcoming Reward");
                    }
                    index++;
            } else {
                lore.add("Percent Chance: " + 100/Main.getPlugin().rewardsLoader.getDailyItemsArray().size() + "%");
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.addItem(item);
        }
    }
}
