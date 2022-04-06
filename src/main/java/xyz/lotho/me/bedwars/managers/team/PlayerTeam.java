package xyz.lotho.me.bedwars.managers.team;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public enum PlayerTeam {

    RED(ChatColor.RED, Color.RED, 14),
    BLUE(ChatColor.BLUE, Color.BLUE, 11),
    GREEN(ChatColor.GREEN, Color.GREEN, 5),
    YELLOW(ChatColor.YELLOW, Color.YELLOW, 4),
    AQUA(ChatColor.AQUA, Color.AQUA, 3),
    WHITE(ChatColor.WHITE, Color.WHITE, 0),
    PINK(ChatColor.LIGHT_PURPLE, Color.FUCHSIA, 6),
    GRAY(ChatColor.GRAY, Color.GRAY, 7);

    private final ChatColor teamColor;
    private final Color armorColor;
    private final int metaID;

    private PlayerTeam(ChatColor teamColor, Color armorColor, int metaID) {
        this.teamColor = teamColor;
        this.armorColor = armorColor;
        this.metaID = metaID;
    }

    public ChatColor getTeamColor() {
        return this.teamColor;
    }

    public Color getArmorColor() {
        return this.armorColor;
    }

    public int getMetaID() {
        return this.metaID;
    }
}
