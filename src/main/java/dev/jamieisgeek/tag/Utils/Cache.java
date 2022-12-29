package dev.jamieisgeek.tag.Utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cache {
    private static Cache cache;
    private List<UUID> queue = new ArrayList<>();
    private boolean inProgress;
    private List<Player> frozenPlayers = new ArrayList<>();
    private Player hunter;
    private List<Player> alivePlayers = new ArrayList<>();
    private int duration;

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void addAlivePlayer(Player player) {
        alivePlayers.add(player);
    }

    public void removeAlivePlayer(Player player) {
        alivePlayers.remove(player);
    }

    public List<Player> getAlivePlayers() {
        return alivePlayers;
    }

    public void setHunter(Player player) {
        hunter = player;
    }

    public Player getHunter() {
        return hunter;
    }

    public void addFrozenPlayer(Player player) {
        frozenPlayers.add(player);
    }

    public void removeFrozenPlayer(Player player) {
        frozenPlayers.remove(player);
    }

    public List<Player> getFrozenPlayers() {
        return frozenPlayers;
    }

    public void addPlayerToQueue(UUID uuid) {
        queue.add(uuid);
    }

    public void removePlayerFromQueue(UUID uuid) {
        queue.remove(uuid);
    }

    public List<UUID> getQueue() {
        return queue;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public Cache() {
        cache = this;
    }

    public static Cache getCache() {
        return cache;
    }
}
