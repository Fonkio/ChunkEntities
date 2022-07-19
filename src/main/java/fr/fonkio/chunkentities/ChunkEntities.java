package fr.fonkio.chunkentities;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class ChunkEntities extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getConsoleSender().sendMessage("§2[ChunkEntities] §rPlugin start");
        PluginCommand commandEntities = getCommand("entities");
        if (commandEntities != null) {
            commandEntities.setExecutor(new CommandEntite(getConfig()));
        }
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("§2[ChunkEntities] §rPlugin stop");
    }

}
