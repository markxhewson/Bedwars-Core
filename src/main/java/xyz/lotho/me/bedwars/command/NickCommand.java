package xyz.lotho.me.bedwars.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.util.Chat;

public class NickCommand implements CommandExecutor {

    private final Bedwars instance;

    public NickCommand(Bedwars instance) {
        this.instance = instance;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage(Chat.color("&cInvalid usage! Usage: &7/nick [name | off]"));
            return true;
        }

        String playerName = args[0];
        if (playerName.equalsIgnoreCase("off")) {
            this.instance.getDisguiseManager().deleteDisguise(player);
            player.sendMessage(Chat.color("&aYou have removed your nick."));
            return true;
        }

        this.instance.getDisguiseManager().loadDisguiseInfo(playerName, ((texture, signature) -> {
            if (texture == null || signature == null) {
                player.sendMessage(ChatColor.RED + "Failed to find \"" + playerName + "\"'s skin.");
                return;
            }

            this.instance.getDisguiseManager().applyDisguise(player, playerName, texture, signature);
            player.sendMessage(Chat.color("&aYou have nicked as " + playerName + " successfully."));
        }));

        return true;
    }
}
