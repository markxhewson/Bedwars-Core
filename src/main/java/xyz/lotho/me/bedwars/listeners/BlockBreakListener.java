package xyz.lotho.me.bedwars.listeners;

import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.managers.teams.Team;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class BlockBreakListener implements Listener {

    private final Bedwars instance;

    public BlockBreakListener(Bedwars instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        System.out.println("hey1");
        Player player = event.getPlayer();
        Block block = event.getBlock();

        Game game = this.instance.getGameManager().findPlayerGame(player.getUniqueId());
        if (game == null) return;

        System.out.println("hey2");

        if (block.getType() == Material.BED_BLOCK) {
            System.out.println("hey3");
            AtomicDouble check = new AtomicDouble(0);
            AtomicReference<Team> nearestTeam = new AtomicReference<>();

            game.getTeamManager().getTeamsMap().forEach((teamName, team) -> {
                double angle = Math.toDegrees(Math.atan2(team.getSpawnLocation().getZ() - block.getZ(), team.getSpawnLocation().getX() - block.getX()));
                System.out.println(angle);
                System.out.println(teamName);
                if (angle <= check.get()) {
                    System.out.println("aa");
                    check.set(angle);
                    nearestTeam.set(team);
                }
            });

            player.sendMessage(check.toString());
            player.sendMessage("You broke " + nearestTeam.get().getTeamName() +"'s bed!");
        }
    }
}
