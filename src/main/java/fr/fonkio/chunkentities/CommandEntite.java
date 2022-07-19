package fr.fonkio.chunkentities;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandEntite implements CommandExecutor {

    private final FileConfiguration config;

    public CommandEntite(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1 && args[0].equalsIgnoreCase("list")) {//commande admin
                Player player = (Player) sender;
                if (player.hasPermission("chunkentities.admin")) {
                    World w = player.getWorld();
                    Map<String, Integer> listeNbEntite = new HashMap<>();
                    boolean find = false;
                    for (Entity e : w.getChunkAt(player.getLocation()).getEntities()) {
                        find = true;
                        if (listeNbEntite.containsKey(e.getName())) {
                            listeNbEntite.put(e.getName(), listeNbEntite.get(e.getName()) + 1);
                        } else {
                            listeNbEntite.put(e.getName(), 1);
                        }
                    }
                    if (find) {
                        sendMessage(sender, config.getString("cmd-list-title"));
                        for (String e : listeNbEntite.keySet()) {
                            player.sendMessage("§7- " + e + " §6x" + listeNbEntite.get(e));
                        }
                        sendMessage(sender,config.getString("cmd-list-total") + " " + w.getChunkAt(player.getLocation()).getEntities().length);
                    } else {
                        config.getString("cmd-list-no-entity");
                    }

                } else {
                    sendMessage(sender, "§4" + config.getString("cmd-list-no-permission"));
                }

            } else {
                commandeEntiteJoueur(sender);
            }


        } else {
            sendMessage(sender, config.getString("command-in-console"));
        }

        return true;
    }

    private void sendMessage(CommandSender sender, String message) {
        sender.sendMessage("§6§l[ChunkEntity] §r§7"+ message);
    }

    public void commandeEntiteJoueur(CommandSender sender) {
        Player player = (Player) sender;
        World w = player.getWorld();

        List<Chunk> chunks = new ArrayList<>();
        boolean sup = false; // Savoir si une limite a été dépassée

        chunks.add(w.getChunkAt(player.getLocation().add(-16f, 0f, -16f))); //NW
        chunks.add(w.getChunkAt(player.getLocation().add(0f, 0f, -16f))); //N
        chunks.add(w.getChunkAt(player.getLocation().add(16f, 0f, -16f))); //NE
        chunks.add(w.getChunkAt(player.getLocation().add(-16f, 0f, 0f))); //W
        chunks.add(w.getChunkAt(player.getLocation()));                    //Player position
        chunks.add(w.getChunkAt(player.getLocation().add(16f, 0f, 0f)));    //E
        chunks.add(w.getChunkAt(player.getLocation().add(-16f, 0f, 16f))); //SW
        chunks.add(w.getChunkAt(player.getLocation().add(0f, 0f, 16f)));  //S
        chunks.add(w.getChunkAt(player.getLocation().add(16f, 0f, 16f))); //SE

        ArrayList<String> nbEntites = new ArrayList<>();

        for (Chunk chunk : chunks) { //Parcours des chunks pour recup le nombre d'entité
            int nb = nbEntity(chunk);
            sup = sup || nb > 25;
            nbEntites.add(nbFormatMap(nb));
        }


        String output = String.format(
                config.getString("cmd-entities-title") + "\n" +
                        "§7          N\n" +
                        "§7  ==============\n" +
                        "§7  | %s | %s | %s |\n" +
                        "§7  ==============\n" +
                        "§7W | %s | %s | %s | E\n" +
                        "§7  ==============\n" +
                        "§7  | %s | %s | %s |\n" +
                        "§7  ==============\n" +
                        "§7          S\n",
                nbEntites.toArray());

        sendMessage(sender, output);

        if (sup) {
            sendMessage(sender, config.getString("cmd-entities-warning"));
        }
    }

    public int nbEntity(Chunk chunk) {
        Entity[] tabEnt = chunk.getEntities();
        return tabEnt.length;
    }

    public String nbFormatMap(int nbe) {
        Integer nb = nbe;
        if (nb > 999) {
            return "§4999§7"; //Pour ne pas décaler le tableau
        } else if (nb > 99) {
            StringBuilder s = new StringBuilder();
            if (nb > config.getInt("warning")) {
                s.append("§4");
            } else if (nb > config.getInt("info")) {
                s.append("§6");
            }
            return s.append(nb).append("§7").toString();
        } else if (nb > 9) {
            StringBuilder s = new StringBuilder();
            if (nb > config.getInt("warning")) {
                s.append("§4");
            } else if (nb > config.getInt("info")) {
                s.append("§6");
            }
            return s.append(" ").append(nb).append("§7").toString();
        }
        return " " + nb + " §7";
    }

}
