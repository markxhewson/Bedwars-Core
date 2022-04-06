package xyz.lotho.me.bedwars.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.util.Chat;

public class KaboomCommand implements CommandExecutor {

    private final Bedwars instance;

    public KaboomCommand(Bedwars instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] strings) {
        if (!(sender instanceof Player)) return false;

        this.instance.getServer().getOnlinePlayers().forEach(player -> {
            for (int i = 0; i < 5; i++) {
                player.getWorld().strikeLightningEffect(player.getLocation());
            }

            Vector upVector = new Vector(0, 4, 0);
            player.setVelocity(upVector);

            player.sendMessage(Chat.color("&aKaboom!"));
        });

        return true;
    }
}
