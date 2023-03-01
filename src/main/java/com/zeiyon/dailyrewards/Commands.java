package com.zeiyon.dailyrewards;

import com.zeiyon.dailyrewards.menus.MainMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;

        Player p = (Player) sender;
        MainMenu menu = new MainMenu(Main.getPlayerMenUtility(p));
        menu.open();

        return false;
    }


}
