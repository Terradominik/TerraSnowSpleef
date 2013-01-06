package me.terradominik.plugins.terrasnowspleef;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import me.terradominik.plugins.terrasnowspleef.Event.RundenFiler;
import me.terradominik.plugins.terraworld.TerraWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * die Command Klasse von TerraSnowSpleef hier werden alle Commands bearbeitet
 *
 * @author Terradominik
 */
public class Commands {
    
    private TerraSnowSpleef plugin;

    /**
     * der Konstruktor von "Commands"
     *
     * @param plugin
     */
    public Commands(TerraSnowSpleef plugin) {
        this.plugin = plugin;
    }

    /**
     * lässt den Spieler beitreten
     *
     * @param spieler
     */
    public void beitreten(Player spieler) {

        //Promote
        if (!spieler.hasPermission("terraworld.spieler")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + spieler.getName() + " group set spiele");
        }
        
        Spiel spiel = plugin.getSpiel();
        if (!plugin.getConfig().getBoolean("event")) {
            if (!spiel.getJoinCountdown()) {
                if (!spiel.getSpiel() && !spiel.getStartCountdown()) {
                    spiel = new Spiel(plugin);
                    spiel.starteJoinCountdown();
                    this.addSpieler(spieler);
                } else {
                    plugin.sendMessage(spieler, "Das Spiel hat leider schon begonnen :(");
                }
            } else {
                this.addSpieler(spieler);
            }
        } else plugin.sendMessage(spieler, "Bitte melde dich noch schnell bei einem Admin falls du noch nicht eingetragen bist");
    }
    
    public void addSpieler(Player spieler) {
        if (plugin.getSpiel().getSpielerSet().add(spieler.getName())) {
            plugin.broadcastMessage(ChatColor.GOLD + spieler.getName() + ChatColor.GRAY + " hat SnowSpleef betreten");
            TerraWorld.addSpieler(spieler);
            String tabname = ChatColor.WHITE + "SS " + ChatColor.AQUA + spieler.getName();
            if (tabname.length() > 16) {
                spieler.setPlayerListName(tabname.substring(0, 14) + "..");
            } else {
                spieler.setPlayerListName(tabname);
            }
        }
        if (Filer.getConfig().getString(spieler.getName() + ".GespielteRunden") == null) {
            Filer.getConfig().set(spieler.getName() + ".GespielteRunden", 0);
        }
        if (Filer.getConfig().getString(spieler.getName() + ".GewonneneRunden") == null) {
            Filer.getConfig().set(spieler.getName() + ".GewonneneRunden", 0);
        }
        
    }

    /**
     * das hilfe Command
     *
     * @param spieler
     */
    public void hilfe(Player spieler) {
        spieler.sendMessage(ChatColor.DARK_AQUA + "[TSS]: " + ChatColor.GRAY + "Folgende Commands stehen dir zur Verfügung:");
        spieler.sendMessage(ChatColor.GOLD + "  /tss beitreten " + ChatColor.GRAY + "Lässt dich das Spiel beitreten");
        spieler.sendMessage(ChatColor.GOLD + "  /tss statistik [Spieler] " + ChatColor.GRAY + "Ruft eine Statistik des Spielers auf");
        spieler.sendMessage(ChatColor.GOLD + "  /tss hilfe " + ChatColor.GRAY + "Um diese Hilfe anzuzeigen");
    }

    /**
     * ruft die Statistik des Spielers auf
     *
     * @param spieler
     */
    public void statistik(Player spieler) {
        String target = spieler.getName();
        
        int z1, z2;
        if (Filer.getConfig().getString(target + ".GewonneneRunden") != null) {
            z1 = Integer.parseInt(Filer.getConfig().getString(target + ".GewonneneRunden"));
        } else {
            z1 = 0;
        }
        if (Filer.getConfig().getString(target + ".GespielteRunden") != null) {
            z2 = Integer.parseInt(Filer.getConfig().getString(target + ".GespielteRunden"));
        } else {
            z2 = 0;
        }
        
        spieler.sendMessage(ChatColor.DARK_AQUA + "[TSS]: " + ChatColor.GOLD + target + ChatColor.GRAY + ": ");
        spieler.sendMessage(ChatColor.GRAY + "  Gespielte Runden: " + ChatColor.GOLD + z1);
        spieler.sendMessage(ChatColor.GRAY + "  Gewonnene Runden: " + ChatColor.GOLD + z2);
    }

    /**
     * ruft die Statistik eines anderen Spielers auf
     *
     * @param spieler
     * @param target
     */
    public void statistik(Player spieler, String target) {
        if (plugin.getServer().getPlayer(target) != null) {
            target = plugin.getServer().getPlayer(target).getName();
        } else {
            target = plugin.getServer().getOfflinePlayer(target).getName();
        }
        
        int z1, z2;
        if (Filer.getConfig().getString(target + ".GewonneneRunden") != null) {
            z1 = Integer.parseInt(Filer.getConfig().getString(target + ".GewonneneRunden"));
        } else {
            z1 = 0;
        }
        if (Filer.getConfig().getString(target + ".GespielteRunden") != null) {
            z2 = Integer.parseInt(Filer.getConfig().getString(target + ".GespielteRunden"));
        } else {
            z2 = 0;
        }
        
        spieler.sendMessage(ChatColor.DARK_AQUA + "[TSS]: " + ChatColor.GOLD + target + ChatColor.GRAY + ": ");
        spieler.sendMessage(ChatColor.GRAY + "  Gespielte Runden: " + ChatColor.GOLD + z1);
        spieler.sendMessage(ChatColor.GRAY + "  Gewonnene Runden: " + ChatColor.GOLD + z2);
    }

    /**
     * setzt verschiedene Config Variablen
     *
     * @param spieler
     * @param cmd
     * @param param
     */
    public void set(Player spieler, String cmd, String param) {
        Location loc = spieler.getTargetBlock(null, 500).getLocation();
        switch (cmd) {
            case "p1":
                plugin.getConfig().set("xPunkt", loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
                plugin.getConfig().set("Welt", loc.getWorld().getName());
                plugin.saveConfig();
                plugin.reloadConfig();
                plugin.sendMessage(spieler, "Punkt1 wurde erfolgreich gesetzt");
                break;
            case "p2":
                plugin.getConfig().set("yPunkt", loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
                plugin.saveConfig();
                plugin.reloadConfig();
                plugin.sendMessage(spieler, "Punkt2 wurde erfolgreich gesetzt");
                break;
            case "ebenen-anzahl":
                try {
                    plugin.getConfig().set("Ebenen-Anzahl", Integer.parseInt(param));
                    plugin.saveConfig();
                    plugin.reloadConfig();
                    plugin.sendMessage(spieler, "Die Ebenen-Anzahl wurde erfolgreich auf " 
                            + ChatColor.GOLD + Integer.parseInt(param) + ChatColor.GRAY + " gesetzt");
                } catch (NumberFormatException nfe) {
                    plugin.sendMessage(spieler, "Gib einen Zahl ein!");
                }
                break;
            case "boden":
                plugin.getConfig().set("Boden", loc.getBlockY());
                plugin.saveConfig();
                plugin.reloadConfig();
                plugin.sendMessage(spieler, "Die Bodenhöhe wurde erfolgreich gesetzt");
                break;
            case "totspawn":
                Location totloc = spieler.getLocation();
                plugin.getConfig().set("TotSpawn", 
                        totloc.getWorld().getName() + "," +
                        totloc.getBlockX() + "," + 
                        totloc.getBlockY() + "," + 
                        totloc.getBlockZ() + "," + 
                        totloc.getYaw() + "," + 
                        totloc.getPitch());
                plugin.sendMessage(spieler, "Totspanw erfolgreich gesetzt");
                break;
        }
    }

    /**
     * ruft eine Liste der Spieler im Spiel auf
     *
     * @param spieler
     */
    public void liste(Player spieler) {
        String spielerliste = "";
        Iterator<String> it = plugin.getSpiel().getSpielerSet().iterator();
        while (it.hasNext()) {
            spielerliste += it.next() + ", ";
        }
        
        spieler.sendMessage(ChatColor.DARK_AQUA + "[TSS]: " + ChatColor.GRAY + "Folgende Spieler sind noch im Spiel:");
        spieler.sendMessage(ChatColor.GRAY + spielerliste);
    }

    /**
     * Ein Command welches zum reinen testen dient
     *
     * @param spieler
     * @param param
     */
    public void test(Player spieler, String param) {
        switch (param) {
            case "spielfeld":
                TerraSnowSpleef.sendMessage(spieler, "DEBUG: " + plugin.getSpiel().getSpielfeld().inSpielfeld(spieler.getLocation()));
                break;
        }
    }

    /**
     * Event Commands
     *
     * @param spieler
     * @param name
     * @param param
     */
    public void event(Player spieler, String name, String[] param) {
        switch (name) {
            case "togglemode":
                plugin.getConfig().set("event", !plugin.getConfig().getBoolean("event", true));
                TerraSnowSpleef.sendMessage(spieler, "Der EventModus wurde auf " + plugin.getConfig().getBoolean("event") + " gesetzt");
                break;
            case "starterunde":
                Spiel spiel = plugin.getSpiel();
                spiel = new Spiel(plugin);
                spiel.starteJoinCountdown();
                List<String> rundenSpieler = RundenFiler.getConfig().getStringList("runden." + param[0]);
                for (String spielerString : rundenSpieler) {
                    this.addSpieler(spieler);
                }
                break;
            case "addspieler":
                HashSet<String> spielerAddListe = new HashSet<>(RundenFiler.getConfig().getStringList("runden." + param[0]));
                try {
                    spielerAddListe.add(plugin.getServer().getPlayer(param[1]).getName());
                    TerraSnowSpleef.sendMessage(spieler, plugin.getServer().getPlayer(param[1]).getName() + " wurde zur Liste " + param[0] + " hinzugefügt");
                }
                catch (NullPointerException npe) {
                    spielerAddListe.add(param[1]);
                    TerraSnowSpleef.sendMessage(spieler, param[1] + " wurde zur Liste " + param[0] + " hinzugefügt");
                };
                RundenFiler.getConfig().set("runden." + param[0], spielerAddListe);
                break;
            case "removespieler":
                HashSet<String> spielerRemoveListe = new HashSet<>(RundenFiler.getConfig().getStringList("runden." + param[0]));
                try {
                    spielerRemoveListe.add(plugin.getServer().getPlayer(param[1]).getName());
                    TerraSnowSpleef.sendMessage(spieler, plugin.getServer().getPlayer(param[1]).getName() + " wurde von der Liste " + param[0] + " gelöscht");
                }
                catch (NullPointerException npe) {
                    spielerRemoveListe.add(param[1]);
                    TerraSnowSpleef.sendMessage(spieler, param[1] + " wurde von der Liste " + param[0] + " gelöscht");
                };
                RundenFiler.getConfig().set("runden." + param[0], spielerRemoveListe);
                break;
            case "showliste":
                List<String> spielerShowListe = RundenFiler.getConfig().getStringList("runden." + param[0]);
                String ausgabe = "";
                for (String target : spielerShowListe) {
                    ausgabe += target + ", ";
                }
                TerraSnowSpleef.sendMessage(spieler, "Liste " + param[0] + " (" + spielerShowListe.size() + " Einträge):");
                TerraSnowSpleef.sendMessage(spieler, ausgabe);
                break;
            case "eventspawn":
                Location eventspawn = spieler.getLocation();
                plugin.getConfig().set("event.spawn", 
                        eventspawn.getWorld().getName() + "," +
                        eventspawn.getBlockX() + "," + 
                        eventspawn.getBlockY() + "," + 
                        eventspawn.getBlockZ() + "," + 
                        eventspawn.getYaw() + "," + 
                        eventspawn.getPitch());
                plugin.sendMessage(spieler, "Event Spawn erfolgreich gesetzt");
                break;
            case "stop":
                HashSet<String> spielerSet = (HashSet<String>) plugin.getSpiel().getSpielerSet().clone();
                plugin.neuesSpiel();
                switch(param[0].charAt(0)) {
                    case '1':
                        List<String> liste1 = RundenFiler.getConfig().getStringList("runden.1");
                        List<String> liste2 = new ArrayList<>();
                        List<String> liste3 = new ArrayList<>();
                        for (String spielerString : liste1) {
                            if(spielerSet.contains(spielerString)) liste2.add(spielerString);
                            else liste3.add(spielerString);
                        }
                        RundenFiler.getConfig().set("runden.2", liste2);
                        RundenFiler.getConfig().set("runden.q", liste3);
                        break;
                    case 'q':
                        List<String> liste4 = RundenFiler.getConfig().getStringList("runden.2");
                        for (String spielerString : spielerSet) {
                            liste4.add(spielerString);
                        }
                        RundenFiler.getConfig().set("runden.2", liste4);
                        break;
                    case '2':
                        List<String> liste5 = new ArrayList<>();
                        for (String spielerString : spielerSet) {
                            liste5.add(spielerString);
                        }
                        RundenFiler.getConfig().set("runden.3", liste5);
                        break;
                }
                break;
        }
    }
}
