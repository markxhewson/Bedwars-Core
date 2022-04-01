package xyz.lotho.me.bedwars.managers.teams;

import org.bukkit.ChatColor;

public enum PlayerTeam {

    RED(ChatColor.RED),
    BLUE(ChatColor.DARK_BLUE),
    GREEN(ChatColor.GREEN),
    YELLOW(ChatColor.YELLOW),
    AQUA(ChatColor.AQUA),
    WHITE(ChatColor.WHITE),
    PINK(ChatColor.LIGHT_PURPLE),
    GRAY(ChatColor.GRAY);

    private final ChatColor teamColor;

    private PlayerTeam(ChatColor teamColor) {
        this.teamColor = teamColor;
    }

    public ChatColor getTeamColor() {
        return this.teamColor;
    }
}
