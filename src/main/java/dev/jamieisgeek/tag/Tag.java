package dev.jamieisgeek.tag;

import dev.jamieisgeek.CommandRegisterer;
import dev.jamieisgeek.EventRegisterer;
import dev.jamieisgeek.tag.Utils.Manager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

public final class Tag extends JavaPlugin {

    @Override
    public void onEnable() {
        try {
            new CommandRegisterer(this, getClass().getPackage().getName(), "Commands").registerCommands();
            new EventRegisterer(this, getClass().getPackage().getName(), "Listeners").registerEvents();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        saveDefaultConfig();
        new Manager(this, this.getConfig());

        getLogger().info("Tag has been enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
