package com.zeiyon.dailyrewards;

import com.zeiyon.dailyrewards.files.RewardsLoader;
import com.zeiyon.dailyrewards.menus.AllRewardsMenu;
import com.zeiyon.dailyrewards.menus.DailyRewardsMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;

        Player p = (Player) sender;
        AllRewardsMenu menu = new AllRewardsMenu(Main.getPlayerMenUtility(p));
        menu.open();

        return false;
    }


}
