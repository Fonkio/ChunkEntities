package fr.fonkio.chunkentities;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandEntite implements CommandExecutor {

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
                        player.sendMessage("§6§l[Chunk Entity] §r§7Liste des entités :");
                        for (String e : listeNbEntite.keySet()) {
                            player.sendMessage("§7- " + e + " §6x" + listeNbEntite.get(e));
                        }
                        player.sendMessage("§7Nombre total : " + w.getChunkAt(player.getLocation()).getEntities().length);
                    } else {
                        player.sendMessage("§6§l[Chunk Entity] §r§7Il n'y a pas d'entité");
                    }

                } else {
                    player.sendMessage("§6§l[Chunk Entity] §r§4Vous n'avez pas la permission");
                }

            } else {
                commandeEntiteJoueur(sender);
            }


        } else {
            sender.sendMessage("[ChunkEntities] Cette commande ne peut etre executee uniquement par un joueur");
        }

        return true;
    }

    public void commandeEntiteJoueur(CommandSender sender) {
        Player player = (Player) sender;
        World w = player.getWorld();

        List<Chunk> chunks = new ArrayList<Chunk>();
        boolean sup = false; // Savoir si une limite a été dépassée

        chunks.add(w.getChunkAt(player.getLocation().add(-16f, 0f, -16f))); //NO
        chunks.add(w.getChunkAt(player.getLocation().add(0f, 0f, -16f))); //N
        chunks.add(w.getChunkAt(player.getLocation().add(16f, 0f, -16f))); //NE
        chunks.add(w.getChunkAt(player.getLocation().add(-16f, 0f, 0f))); //O
        chunks.add(w.getChunkAt(player.getLocation()));                    //Position du joueur
        chunks.add(w.getChunkAt(player.getLocation().add(16f, 0f, 0f)));    //E
        chunks.add(w.getChunkAt(player.getLocation().add(-16f, 0f, 16f))); //SO
        chunks.add(w.getChunkAt(player.getLocation().add(0f, 0f, 16f)));  //S
        chunks.add(w.getChunkAt(player.getLocation().add(16f, 0f, 16f))); //SE

        ArrayList<String> nbEntites = new ArrayList<String>();

        for (Chunk chunk : chunks) { //Parcours des chunks pour recup le nombre d'entité
            int nb = nbEntity(chunk);
            sup = sup || nb > 25;
            nbEntites.add(nbFormatMap(nb));
        }


        String output = String.format(
                "§6§l[Chunk Entity] §r§7Carte des chunks aux alentours :\n" +
                        "§7          N\n" +
                        "§7  ==============\n" +
                        "§7  | %s | %s | %s |\n" +
                        "§7  ==============\n" +
                        "§7O | %s | %s | %s | E\n" +
                        "§7  ==============\n" +
                        "§7  | %s | %s | %s |\n" +
                        "§7  ==============\n" +
                        "§7          S\n",
                nbEntites.toArray());

        player.sendMessage(output);

        if (sup) {

            player.sendMessage("§6ATTENTION : La limite autorisée est de 30 entités par chunk !");
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
            return "§4" + nb.toString() + "§7";
        } else if (nb > 9) {
            StringBuilder s = new StringBuilder();
            if (nb > 30) {
                s.append("§4");
            } else if (nb > 25) {
                s.append("§6");
            }
            return s.append(" ").append(nb).append("§7").toString();
        }
        return " " + nb + " §7";
    }

}
