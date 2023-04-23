package com.zeiyon.dailyrewards.managers;

import com.zeiyon.dailyrewards.Main;
import com.zeiyon.dailyrewards.files.MessagesFile;
import com.zeiyon.dailyrewards.files.RewardsFile;
import com.zeiyon.dailyrewards.menus.RewardsMenu;
import com.zeiyon.dailyrewards.menus.MainMenu;
import com.zeiyon.dailyrewards.menus.WeeklyRewardsMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandsManager implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //Main DailyRewards Command. Opens the main menu.
        if (sender instanceof Player && args.length == 0) {
            Player p = (Player) sender;
            MainMenu menu = new MainMenu(Main.getPlayerMenUtility(p));
            menu.open();
        }
        if (args.length >= 1) {


            //Help Command Argument
            if (args[0].equalsIgnoreCase("help")) {
                for(String str : MessagesFile.getFileConfig().getStringList("help-command")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', str));
                }
                if ((sender instanceof Player && sender.hasPermission("dr.admin")) || sender instanceof ConsoleCommandSender) {
                    //Admin-Help Command Argument
                    for(String str : MessagesFile.getFileConfig().getStringList("help-command-admin")) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', str));
                    }
                }
            }


            //Claim Argument
            if (args[0].equalsIgnoreCase("claim") && sender instanceof Player) {
                Player p = (Player) sender;
                RewardsManager.claimDailyReward(p);
            }


            //Rewards Argument
            if ((args[0].equalsIgnoreCase("reward") || args[0].equalsIgnoreCase("rewards")) && sender instanceof Player) {
                Player p = (Player) sender;
                RewardsMenu menu = new RewardsMenu(Main.getPlayerMenUtility(p));
                menu.open();
            }


            //Weekly-Rewards Argument
            if ((args[0].equalsIgnoreCase("weeklyreward") || args[0].equalsIgnoreCase("weeklyrewards")) && sender instanceof Player) {
                Player p = (Player) sender;
                WeeklyRewardsMenu menu = new WeeklyRewardsMenu(Main.getPlayerMenUtility(p));
                menu.open();
            }



            //All Arguments beyond this point are admin only
            if ((sender instanceof Player && sender.hasPermission("dr.admin")) || sender instanceof ConsoleCommandSender) {
                //Add/Remove Time Argument
                if (args[0].equalsIgnoreCase("addTime") || args[0].equalsIgnoreCase("removeTime")) {
                    if (args.length == 1) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessagesFile.getFileConfig().getString("dr-addTime-notvalid")));
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessagesFile.getFileConfig().getString("dr-addTime-usage")));
                    } else {
                        String potentialPlayer = args[1];
                        if (Bukkit.getPlayerExact(potentialPlayer) == null) {

                            sender.sendMessage("\"" + args[1] + "\"" + " is not a valid/online player");
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessagesFile.getFileConfig().getString("dr-addTime-usage")));
                        } else {
                            Player argPlayer = Bukkit.getPlayer(potentialPlayer);
                            try {
                                Integer.parseInt(args[2]);
                                if (args[0].equalsIgnoreCase("addTime")) {
                                    addTime(sender, argPlayer, Integer.parseInt(args[2]) * 1000);
                                } else if (args[0].equalsIgnoreCase("removeTime")) {
                                    removeTime(sender, argPlayer, Integer.parseInt(args[2]) * 1000);
                                }
                            } catch (final NumberFormatException e) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessagesFile.getFileConfig().getString("dr-addTime-notvalidnumber")));
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessagesFile.getFileConfig().getString("dr-addTime-usage")));
                            }
                        }
                    }
                }


                //Reset Argument
                if (args[0].equalsIgnoreCase("reset")) {
                    if (args.length == 1) {
                        sender.sendMessage("Please enter a valid user");
                        sender.sendMessage("usage: /dr reset {user}");
                    } else {
                        String potentialPlayer = args[1];
                        if (Bukkit.getPlayerExact(potentialPlayer) == null) {
                            sender.sendMessage("\"" + args[1] + "\"" + " is not a valid/online player");
                            sender.sendMessage("usage: /dr reset {user}");
                        } else {
                            Player argPlayer = Bukkit.getPlayer(potentialPlayer);
                            if (CooldownsManager.getDailyCooldown().asMap().containsKey(argPlayer.getUniqueId())) {
                                CooldownsManager.getDailyCooldown().asMap().remove(argPlayer.getUniqueId());
                                sender.sendMessage(argPlayer.getDisplayName() + "'s reward has been reset.");
                            } else
                                sender.sendMessage(argPlayer.getDisplayName() + "'s reward is currently available to claim.");
                        }
                    }
                }


                //View-Time Argument
                if (args[0].equalsIgnoreCase("viewtime")) {
                    if (args.length == 1) {
                        sender.sendMessage("Please enter a valid user");
                        sender.sendMessage("usage: /dr viewTime {user}");
                    } else {
                        String potentialPlayer = args[1];
                        if (Bukkit.getPlayerExact(potentialPlayer) == null) {
                            sender.sendMessage("\"" + args[1] + "\"" + " is not a valid/online player");
                            sender.sendMessage("usage: /dr viewTime {user}");
                        } else {
                            Player argPlayer = Bukkit.getPlayer(potentialPlayer);
                            if (CooldownsManager.getDailyCooldown().asMap().containsKey(argPlayer.getUniqueId())) {
                                sender.sendMessage(argPlayer.getDisplayName() + "'s remaining time is " + CooldownsManager.dailyHoursRemaining(argPlayer) + " Hours " + CooldownsManager.dailyMinutesRemaining(argPlayer) + " Minutes.");
                            } else
                                sender.sendMessage(argPlayer.getDisplayName() + "'s reward is currently available to claim.");
                        }
                    }
                }


                //Reload Argument
                if (args[0].equalsIgnoreCase("reload")) {
                    Main.getPlugin().reloadConfig();
                    RewardsFile.reload();
                    sender.sendMessage("Plugin has been reloaded!");
                }
            }
        }
        return true;
    }


    //Methods for adding/removing time, that are called in the arguments above
    private void addTime(CommandSender sender, Player argPlayer, Integer delay) {
        if (CooldownsManager.getDailyCooldown().asMap().containsKey(argPlayer.getUniqueId()) && delay > 0) {
            long temp = (long) CooldownsManager.getDailyCooldown().asMap().get(argPlayer.getUniqueId()) - System.currentTimeMillis();
            CooldownsManager.getDailyCooldown().asMap().remove(argPlayer.getUniqueId());
            CooldownsManager.getDailyCooldown().asMap().put(argPlayer.getUniqueId(), System.currentTimeMillis() + temp + delay);
            sender.sendMessage(argPlayer.getDisplayName() + "'s delay has been increased by " + delay/1000 + " seconds.");
        } else if (delay > 0){
            CooldownsManager.getDailyCooldown().asMap().put(argPlayer.getUniqueId(), System.currentTimeMillis() + delay);
            sender.sendMessage(argPlayer.getDisplayName() + "'s delay has been increased by " + delay/1000 + " seconds.");
        } else if (delay <=0) {
            sender.sendMessage("Please enter a positive integer value.");
        }
    }

    private void removeTime(CommandSender sender, Player argPlayer, Integer delay) {
        if (CooldownsManager.getDailyCooldown().asMap().containsKey(argPlayer.getUniqueId())) {
            long temp = (long) CooldownsManager.getDailyCooldown().asMap().get(argPlayer.getUniqueId()) - System.currentTimeMillis();
            CooldownsManager.getDailyCooldown().asMap().remove(argPlayer.getUniqueId());
            CooldownsManager.getDailyCooldown().asMap().put(argPlayer.getUniqueId(), System.currentTimeMillis() + temp - delay);
            sender.sendMessage(argPlayer.getDisplayName() + "'s delay has been decreased by " + delay/1000 + " seconds.");
        } else sender.sendMessage("Player has not yet claimed their Daily Reward");
    }
}
