package xyz.lotho.me.bedwars.managers.disguise;

import org.bukkit.entity.Player;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.util.disguise.HTTPUtility;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class DisguiseManager {
    private final Bedwars instance;
    private final Map<UUID, Disguise> disguiseMap = new HashMap<>();

    public DisguiseManager(Bedwars instance) {
        this.instance = instance;
    }

    public Map<UUID, Disguise> getDisguiseMap() {
        return this.disguiseMap;
    }

    public void loadDisguiseInfo(String playerName, HTTPUtility.GetTextureResponse response) {
        this.instance.getHttpUtility().getTextureAndSignature(playerName, response);
    }

    public void applyDisguise(Player player, String name, String texture, String signature) {
        if (hasDisguise(player)) {
            this.deleteDisguise(player);
        }

        Disguise disguise = new Disguise(this.instance, name, texture, signature);
        this.getDisguiseMap().put(player.getUniqueId(), disguise);
        disguise.apply(player);
    }

    public void deleteDisguise(Player player) {
        if (!this.hasDisguise(player)) return;
        if (this.getDisguise(player).isPresent()) {
            Disguise disguise = getDisguise(player).get();

            disguise.remove(player);
            this.getDisguiseMap().remove(player.getUniqueId());
        }
    }

    public Optional<Disguise> getDisguise(Player player) {
        return Optional.ofNullable(
                this.getDisguiseMap().get(player.getUniqueId())
        );
    }

    public boolean hasDisguise(Player player) {
        return this.getDisguiseMap().containsKey(player.getUniqueId());
    }
}
