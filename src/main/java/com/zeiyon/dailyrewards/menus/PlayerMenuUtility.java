package com.zeiyon.dailyrewards.menus;

import org.bukkit.entity.Player;

public class PlayerMenuUtility {

    private static Player owner;

    public PlayerMenuUtility(Player owner) {
        PlayerMenuUtility.owner = owner;
    }

    public static Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        PlayerMenuUtility.owner = owner;
    }

}
