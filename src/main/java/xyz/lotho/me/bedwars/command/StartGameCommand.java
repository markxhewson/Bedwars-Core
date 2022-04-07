package xyz.lotho.me.bedwars.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.util.Chat;

import java.util.UUID;

public class StartGameCommand implements CommandExecutor {

    private final Bedwars instance;

    public StartGameCommand(Bedwars instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        if (this.instance.getQueueManager().getQueuedPlayers().size() < 1) {
            sender.sendMessage(Chat.color("&cYou are unable to start a game with less than 2 people in queue."));
            return false;
        }

        Game game = new Game(this.instance, UUID.randomUUID(), this.instance.getGameWorld(), this.instance.getLastGame().add(5000, 0, 0));
        this.instance.getGameManager().addGame(game);

        sender.sendMessage(Chat.color("&aStarting game.. &8" + game.getGameUUID()));
        return true;
    }
}
