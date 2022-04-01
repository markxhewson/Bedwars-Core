package xyz.lotho.me.bedwars.managers.player;

import xyz.lotho.me.bedwars.Bedwars;

import java.util.UUID;

public class GamePlayer {

    private final Bedwars instance;
    private final UUID uuid;

    public GamePlayer(Bedwars instance, UUID uuid) {
        this.instance = instance;
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return this.uuid;
    }
}
