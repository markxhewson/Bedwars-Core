package xyz.lotho.me.bedwars.managers.teams;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public enum PlayerTeam {

    RED(ChatColor.RED, Color.RED),
    BLUE(ChatColor.DARK_BLUE, Color.BLUE),
    GREEN(ChatColor.GREEN, Color.GREEN),
    YELLOW(ChatColor.YELLOW, Color.YELLOW),
    AQUA(ChatColor.AQUA, Color.AQUA),
    WHITE(ChatColor.WHITE, Color.WHITE),
    PINK(ChatColor.LIGHT_PURPLE, Color.FUCHSIA),
    GRAY(ChatColor.GRAY, Color.GRAY);

    private final ChatColor teamColor;
    private final Color armorColor;

    private PlayerTeam(ChatColor teamColor, Color armorColor) {
        this.teamColor = teamColor;
        this.armorColor = armorColor;
    }

    public ChatColor getTeamColor() {
        return this.teamColor;
    }

    public Color getArmorColor() {
        return this.armorColor;
    }
}
