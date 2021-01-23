package fr.fonkio.chunkentities;

import org.bukkit.plugin.java.JavaPlugin;

public class ChunkEntities extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage("§2[ChunkEntities] §rPlugin start");
        getCommand("entite").setExecutor(new CommandEntite());
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("§2[ChunkEntities] §rPlugin stop");
    }

}
