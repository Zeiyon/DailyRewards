package com.zeiyon.dailyrewards;

import com.zeiyon.dailyrewards.files.RewardsFile;
import com.zeiyon.dailyrewards.menus.AllRewardsMenu;
import com.zeiyon.dailyrewards.menus.MainMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {


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
                sender.sendMessage("------------------------[DailyRewards]------------------------");
                sender.sendMessage("- /dr help - This menu");
                sender.sendMessage("- /dr claim - Claims the Daily-Reward");
                sender.sendMessage("- /dr claim weekly - Claims the Weekly-Reward");
                sender.sendMessage("- /dr rewards - Opens the rewards menu");
                if ((sender instanceof Player && sender.hasPermission("dr.admin")) || sender instanceof ConsoleCommandSender) {
                    //Admin-Help Command Argument
                    sender.sendMessage("------------------------[ADMIN]------------------------");
                    sender.sendMessage("- /dr addTime {username} {time} - Add time onto a player's dailyreward");
                    sender.sendMessage("- /dr removeTime {username} {time} - Remove time onto a player's dailyreward");
                    sender.sendMessage("- /dr reset {username} - Reset a player's dailyreward timer");
                    sender.sendMessage("- /dr viewtime {username} - View a player's remaining dailyrewards time");
                }
            }


            //Claim Argument
            if (args[0].equalsIgnoreCase("claim") && sender instanceof Player) {
                Player p = (Player) sender;
                if (!Cooldowns.getDailyCooldown().asMap().containsKey(p.getUniqueId())) {
                    p.sendMessage("Claiming Daily Reward");
                    //Gets random int between 0 and the max amount of rewards and gives the player the reward corresponding with that item.
                    int rand = (int) (Math.random() * Main.getPlugin().rewardsLoader.getItemsArray().size());
                    p.getInventory().addItem(Main.getPlugin().rewardsLoader.getItemsArray().get(rand));
                    Cooldowns.getDailyCooldown().put(p.getUniqueId(), System.currentTimeMillis() + Cooldowns.amountDaily);
                } else {
                    p.sendMessage("Must wait " + Cooldowns.dailyHoursRemaining(p) + " hours & " + Cooldowns.dailyMinutesRemaining(p) + " minutes");
                }
            }


            //Rewards Argument
            if ((args[0].equalsIgnoreCase("reward") || args[0].equalsIgnoreCase("rewards")) && sender instanceof Player) {
                Player p = (Player) sender;
                AllRewardsMenu menu = new AllRewardsMenu(Main.getPlayerMenUtility(p));
                menu.open();
            }



            //All Arguments beyond this point are admin only
            if ((sender instanceof Player && sender.hasPermission("dr.admin")) || sender instanceof ConsoleCommandSender) {
                //Add/Remove Time Argument
                if (args[0].equalsIgnoreCase("addTime") || args[0].equalsIgnoreCase("removeTime")) {
                    if (args.length == 1) {
                        sender.sendMessage("Please enter a valid user");
                        sender.sendMessage("usage: /dr addTime/removeTime {user} {amount}");
                    } else {
                        String potentialPlayer = args[1];
                        if (Bukkit.getPlayerExact(potentialPlayer) == null) {
                            sender.sendMessage("\"" + args[1] + "\"" + " is not a valid/online player");
                            sender.sendMessage("usage: /dr addTime/removeTime {user} {amount}");
                        } else {
                            Player argPlayer = Bukkit.getPlayer(potentialPlayer);
                            try {
                                Integer.parseInt(args[2]);
                                if (args[0].equalsIgnoreCase("addTime")) {
                                    addTime(sender, argPlayer, Integer.valueOf(args[2]) * 1000);
                                } else if (args[0].equalsIgnoreCase("removeTime")) {
                                    removeTime(sender, argPlayer, Integer.valueOf(args[2]) * 1000);
                                }
                            } catch (final NumberFormatException e) {
                                sender.sendMessage("Please enter a valid number.");
                                sender.sendMessage("usage: /dr addTime/removeTime {user} {amount}");
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
                            if (Cooldowns.getDailyCooldown().asMap().containsKey(argPlayer.getUniqueId())) {
                                Cooldowns.getDailyCooldown().asMap().remove(argPlayer.getUniqueId());
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
                            if (Cooldowns.getDailyCooldown().asMap().containsKey(argPlayer.getUniqueId())) {
                                sender.sendMessage(argPlayer.getDisplayName() + "'s remaining time is " + Cooldowns.dailyHoursRemaining(argPlayer) + " Hours " + Cooldowns.dailyMinutesRemaining(argPlayer) + " Minutes.");
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


    private void addTime(CommandSender sender, Player argPlayer, Integer delay) {
        if (Cooldowns.getDailyCooldown().asMap().containsKey(argPlayer.getUniqueId()) && delay > 0) {
            long temp = (long) Cooldowns.getDailyCooldown().asMap().get(argPlayer.getUniqueId()) - System.currentTimeMillis();
            Cooldowns.getDailyCooldown().asMap().remove(argPlayer.getUniqueId());
            Cooldowns.getDailyCooldown().asMap().put(argPlayer.getUniqueId(), System.currentTimeMillis() + temp + delay);
            sender.sendMessage(argPlayer.getDisplayName() + "'s delay has been increased by " + delay/1000 + " seconds.");
        } else if (delay > 0){
            Cooldowns.getDailyCooldown().asMap().put(argPlayer.getUniqueId(), System.currentTimeMillis() + delay);
            sender.sendMessage(argPlayer.getDisplayName() + "'s delay has been increased by " + delay/1000 + " seconds.");
        } else if (delay <=0) {
            sender.sendMessage("Please enter a positive integer value.");
        }
    }

    private void removeTime(CommandSender sender, Player argPlayer, Integer delay) {
        if (Cooldowns.getDailyCooldown().asMap().containsKey(argPlayer.getUniqueId())) {
            long temp = (long) Cooldowns.getDailyCooldown().asMap().get(argPlayer.getUniqueId()) - System.currentTimeMillis();
            Cooldowns.getDailyCooldown().asMap().remove(argPlayer.getUniqueId());
            Cooldowns.getDailyCooldown().asMap().put(argPlayer.getUniqueId(), System.currentTimeMillis() + temp - delay);
            sender.sendMessage(argPlayer.getDisplayName() + "'s delay has been decreased by " + delay/1000 + " seconds.");
        } else sender.sendMessage("Player has not yet claimed their Daily Reward");
    }
}
