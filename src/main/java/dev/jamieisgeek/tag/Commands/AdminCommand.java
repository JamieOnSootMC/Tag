package dev.jamieisgeek.tag.Commands;

import dev.jamieisgeek.CommandHandler;
import dev.jamieisgeek.CommandInfo;
import dev.jamieisgeek.tag.Utils.Cache;
import dev.jamieisgeek.tag.Utils.Manager;
import org.bukkit.command.CommandSender;

@CommandInfo(name = "tagadmin", permission = "tag.admin", requiresPlayer = false)
public class AdminCommand extends CommandHandler {
    @Override
    public void execute(CommandSender sender, String[] args) {
        Cache cache = Cache.getCache();
        Manager manager = Manager.getManager();

        if(args.length == 0) {
            sender.sendMessage(Manager.getManager().getPREFIX() + "Invalid Usage. Correct usage:");
            sender.sendMessage(Manager.getManager().getPREFIX() + "/tagadmin start - Starts a game");
            sender.sendMessage(Manager.getManager().getPREFIX() + "/tagadmin stop - Stops a game");
            sender.sendMessage(Manager.getManager().getPREFIX() + "/tagadmin reload - Reloads the config");
            sender.sendMessage(Manager.getManager().getPREFIX() + "/tagadmin queue - Shows the queue");
            sender.sendMessage(Manager.getManager().getPREFIX() + "/tagadmin clearqueue - Clears the queue");
            return;
        }

        if(args.length == 1) {
            switch (args[0]) {
                case "start":
                    if(cache.isInProgress()) {
                        sender.sendMessage(manager.getPREFIX() + "A game is already in progress!");
                        return;
                    }

                    if(cache.getQueue().size() < 2) {
                        sender.sendMessage(manager.getPREFIX() + "There are not enough players in the queue!");
                        return;
                    }

                    manager.startGame();
                    sender.sendMessage(manager.getPREFIX() + "A game has been started!");
                    break;

                case "stop":
                    if(!cache.isInProgress()) {
                        sender.sendMessage(manager.getPREFIX() + "There is no game in progress!");
                        return;
                    }

                    manager.stopGame();
                    sender.sendMessage(manager.getPREFIX() + "The game has been stopped!");
                    break;

                case "reload":
                    manager.reloadConfig();
                    sender.sendMessage(manager.getPREFIX() + "The config has been reloaded!");
                    break;

                case "queue":
                    if(cache.getQueue().size() == 0) {
                        sender.sendMessage(manager.getPREFIX() + "There are no players in the queue!");
                        return;
                    }

                    sender.sendMessage(manager.getPREFIX() + "Players in the queue:");
                    cache.getQueue().forEach(uuid -> sender.sendMessage(cache.getQueue().toString().replace("[", "").replace("]", "")));
                    break;

                case "clearqueue":
                    if(cache.getQueue().size() == 0) {
                        sender.sendMessage(manager.getPREFIX() + "There are no players in the queue!");
                        return;
                    }

                    manager.clearQueue();
                    sender.sendMessage(manager.getPREFIX() + "The queue has been cleared!");
                    break;
            }
        }
    }
}
