package xyz.lotho.me.bedwars.util;

import org.bukkit.ChatColor;

public class Chat {

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
