package xyz.lotho.me.bedwars.command.game;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.util.Chat;

public class JoinGameCommand implements CommandExecutor {

    private final Bedwars instance;

    public JoinGameCommand(Bedwars instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(Chat.color("&e&lJoin Options:\n&8 - &abedwars &7(max: 16 players per game)"));
            return false;
        }
        else if (args[0].equalsIgnoreCase("bedwars")) {
            this.instance.getQueueManager().enQueue(player.getUniqueId());
            player.sendMessage(Chat.color("&aYou have joined the &eBedwars &aQueue! &7(" + this.instance.getQueueManager().getQueuedPlayers().size() + " currently in queue)"));
        } else {
            player.sendMessage(Chat.color("&e&lJoin Options:\n&8 - &abedwars &7(max: 16 players per game)"));
            return false;
        }

        return true;
    }
}
