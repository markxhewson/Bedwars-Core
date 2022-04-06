package xyz.lotho.me.bedwars.queue;

import xyz.lotho.me.bedwars.Bedwars;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class QueueManager {

    private final Bedwars instance;
    private final ArrayList<UUID> queuedPlayers = new ArrayList<>();

    public QueueManager(Bedwars instance) {
        this.instance = instance;
    }

    public ArrayList<UUID> getQueuedPlayers() {
        return this.queuedPlayers;
    }

    public ArrayList<UUID> getQueuedPlayersLimited(int limit) {
        ArrayList<UUID> players = (ArrayList<UUID>) this.queuedPlayers.stream().limit(limit).collect(Collectors.toList());

        for (UUID uuid : players) {
            this.deQueue(uuid);
        }

        return players;
    }

    public void enQueue(UUID uuid) {
        this.getQueuedPlayers().add(uuid);
    }

    public void deQueue(UUID uuid) {
        this.getQueuedPlayers().remove(uuid);
    }
}
