package dev.jamieisgeek.tag.Commands;

import dev.jamieisgeek.CommandHandler;
import dev.jamieisgeek.CommandInfo;
import dev.jamieisgeek.tag.Utils.Cache;
import dev.jamieisgeek.tag.Utils.Manager;
import org.bukkit.entity.Player;

@CommandInfo(name = "jointag", permission = "tag.queue", requiresPlayer = true)
public class JoinGameCommand extends CommandHandler {
    @Override
    public void execute(Player player, String[] args) {
        Cache cache = Cache.getCache();

        if(cache.getQueue().contains(player.getUniqueId())) {
            player.sendMessage(Manager.getManager().getPREFIX() + "You are already in the queue!");
            return;
        }

        if(cache.isInProgress()) {
            player.sendMessage(Manager.getManager().getPREFIX() + "A game is already in progress! Please wait until a game is available");
            return;
        }

        cache.addPlayerToQueue(player.getUniqueId());
        player.sendMessage(Manager.getManager().getPREFIX() + "You have joined the queue!");
    }
}
