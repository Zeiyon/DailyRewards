package com.zeiyon.dailyrewards;

import com.zeiyon.dailyrewards.menus.MainMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        if (args.length == 0) {
            MainMenu menu = new MainMenu(Main.getPlayerMenUtility(p));
            menu.open();

        } else if (args.length >= 1) {

            //Add Delay
            Player argPlayer = p;
            if (args[0].equalsIgnoreCase("addTime")) {
                String potentialPlayer = args[1];
                if (Bukkit.getPlayerExact(potentialPlayer) == null) {
                    p.sendMessage("That is not a player");
                } else {
                    argPlayer = Bukkit.getPlayer(potentialPlayer);
                    try {
                        Integer.parseInt(args[2]);
                        addTime(p, argPlayer, Integer.valueOf(args[2]) * 1000);
                    } catch (final NumberFormatException e) {
                        p.sendMessage("Please enter a valid number.");
                    }
                }
            }




        }

        return false;
    }



    private void addTime(Player p, Player argPlayer, Integer delay) {
        long temp = (long) Main.cooldown.getDailyCooldown().asMap().get(argPlayer.getUniqueId()) - System.currentTimeMillis();
        Main.cooldown.getDailyCooldown().asMap().remove(argPlayer.getUniqueId());
        Main.cooldown.getDailyCooldown().asMap().put(argPlayer.getUniqueId(), System.currentTimeMillis() + temp + delay);
        p.sendMessage("Time has been added!");
    }

}
