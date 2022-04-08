package xyz.lotho.me.bedwars.command.game;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.ui.main.MapSelectMenu;
import xyz.lotho.me.bedwars.util.Chat;

public class EndGameCommand implements CommandExecutor {

    private final Bedwars instance;

    public EndGameCommand(Bedwars instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Game game = this.instance.getGameManager().findGameByPlayer(((Player) sender).getUniqueId());
        if (game == null) {
            sender.sendMessage(Chat.color("&cYou can only use this command while in-game."));
            return false;
        }

        game.broadcast("&c[GAME] An admin has forcefully ended the game..");

        this.instance.getServer().getScheduler().runTaskLater(this.instance, () -> {
            game.endGame();
            sender.sendMessage(Chat.color("&aSuccessfully ended game..\n&8" + game.getGameUUID()));
        }, 40);

        return true;
    }
}
