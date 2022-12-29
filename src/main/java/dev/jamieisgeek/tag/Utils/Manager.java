package dev.jamieisgeek.tag.Utils;

import dev.jamieisgeek.ScoreboardInterface;
import dev.jamieisgeek.tag.Tag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Manager {
    private final Tag INSTANCE;
    private static Manager manager = null;
    private final Cache cache;
    private String PREFIX;
    private FileConfiguration CONFIG;
    private List<Location> spawnLocations = new ArrayList<>();

    public Manager(Tag instance, FileConfiguration config) {
        INSTANCE = instance;
        manager = this;
        cache = new Cache();
        PREFIX = ChatColor.translateAlternateColorCodes('&', config.getString("chatPrefix"));
        CONFIG = config;

        cache.setDuration(config.getInt("gameDuration"));
    }

    public void startGame() {
        ScoreboardInterface scoreboardInterface = new ScoreboardInterface("&a&lTag", List.of(""));

        setupSpawnLocations();

        cache.setInProgress(true);

        cache.getQueue().forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            player.teleport(this.getRandomLocation());

            cache.addFrozenPlayer(player);
        });

        Bukkit.getScheduler().runTaskLater(INSTANCE, () -> {
            cache.getQueue().forEach(uuid -> {
                Player player = Bukkit.getPlayer(uuid);
                explainGame(player);
            });
        }, 20 * 2);

        assignHunter();
        scoreboardInterface.setLines(List.of("&c&lHunter: &4" + cache.getHunter().getName(), "&a&l Remaining: " + cache.getAlivePlayers().size(), "&b&lTime Remaining: " + getRemainingTime()));

        cache.getQueue().forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            scoreboardInterface.update(player);
        });
    }

    private String getRemainingTime() {
        String string = "";
        int duration = cache.getDuration();

        int minutes = 0;
        int seconds = 0;

        if (duration / 60 / 60 / 24 >= 1) {
            duration -= duration / 60 / 60 / 24 * 60 * 60 * 24;
        }
        if (duration / 60 >= 1) {
            minutes = duration / 60;
            duration -= duration / 60 * 60;
        }
        if (duration >= 1)
            seconds = duration;
        if (minutes <= 9) {
            string = string + "0" + minutes + ":";
        } else {
            string = string + minutes + ":";
        }
        if (seconds <= 9) {
            string = string + "0" + seconds;
        } else {
            string = string + seconds;
        }
        return string;
    }

    private void explainGame(Player player) {
        player.sendMessage(PREFIX + "A random player has been assigned to be the tagger!");
        player.sendMessage(PREFIX + "You must avoid the tagger at all costs");
        player.sendMessage(PREFIX + "If you are tagged, you will be eliminated from the game.");
        player.sendMessage(PREFIX + "The tagger wins if they eliminate all the players, the players win if they remain alive after the game is over!");
    }

    private void assignHunter() {
        Random random = new Random();
        int index = random.nextInt(cache.getQueue().size());

        Player player = Bukkit.getPlayer(cache.getQueue().get(index));
        player.sendMessage(PREFIX + ChatColor.RED + "You are the tagger!");

        cache.getQueue().forEach(uuid -> {
            Player p = Bukkit.getPlayer(uuid);
            if(p != player) {
                p.sendMessage(PREFIX + ChatColor.GREEN + "The tagger is " + player.getName());
            }
        });
    }

    private Location getRandomLocation() {
        return spawnLocations.get(new Random().nextInt(spawnLocations.size()));
    }

    private void setupSpawnLocations() {
        ConfigurationSection section = CONFIG.getConfigurationSection("spawnLocations");
        for(String key : section.getKeys(false)) {
            Location location = new Location(
                    Bukkit.getWorld(section.getString(key + ".world")),
                    section.getDouble(key + ".x"),
                    section.getDouble(key + ".y"),
                    section.getDouble(key + ".z")
            );

            spawnLocations.add(location);
        }
    }

    public void stopGame() {
        ScoreboardInterface scoreboardInterface = new ScoreboardInterface("&a&lTag", List.of(""));
        cache.getQueue().forEach(uuid -> scoreboardInterface.resetScoreboard(Bukkit.getPlayer(uuid)));

        cache.getQueue().forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            player.teleport(new Location(
                    Bukkit.getWorld(CONFIG.getString("hubLocation.world")),
                    CONFIG.getDouble("hubLocation.x"),
                    CONFIG.getDouble("hubLocation.y"),
                    CONFIG.getDouble("hubLocation.z")
            ));
        });

        cache.setInProgress(false);
        cache.setDuration(CONFIG.getInt("gameDuration"));
    }

    public void reloadConfig() {
        INSTANCE.saveConfig();
        this.CONFIG = INSTANCE.getConfig();

        PREFIX = ChatColor.translateAlternateColorCodes('&', CONFIG.getString("chatPrefix"));

        INSTANCE.getLogger().info("Config reloaded!");
    }

    public void clearQueue() {
        cache.getQueue().forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            player.sendMessage(PREFIX + "An admin has cleared the game queue. You have been removed from the queue.");
        });

        cache.getQueue().clear();

        INSTANCE.getLogger().info("Queue cleared by admin!");
    }

    public Tag getINSTANCE() {
        return INSTANCE;
    }

    public static Manager getManager() {
        return manager;
    }

    public Cache getCache() {
        return cache;
    }

    public String getPREFIX() {
        return PREFIX;
    }

    public FileConfiguration getCONFIG() {
        return CONFIG;
    }
}
