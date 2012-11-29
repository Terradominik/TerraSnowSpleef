package me.terradominik.plugins.terrasnowspleef;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.terradominik.plugins.terraworld.TerraWorld;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Die Spiellogik von TerraSnowSpleef
 *
 * @author Dominik
 */
public class Spiel {

    private TerraSnowSpleef plugin;
    private boolean joinCountdown; //ist true wenn der countdown zum beitreten des Spieles läuft
    private boolean startCountdown; //ist true wenn der countdown zum starten des Spieles läuft
    private boolean spiel; //ist true wenn ein spiel läuft
    private HashSet<String> spielerSet = new HashSet<>();
    public static int joinCountdownTask, startCountdownTask, spielTask;
    public static Spielfeld sf;

    /**
     * Der Konstruktor von "Spiel"
     *
     * @param plugin
     */
    public Spiel(TerraSnowSpleef plugin) {
        this.plugin = plugin;
        joinCountdown = false;
        startCountdown = false;
        spiel = false;
        sf = new Spielfeld(plugin);
    }

    /**
     * getter von "joinCountdown"
     *
     * @return joinCountdown
     */
    public boolean getJoinCountdown() {
        return joinCountdown;
    }

    /**
     * getter von "startCountdown"
     *
     * @return startCountdown
     */
    public boolean getStartCountdown() {
        return startCountdown;
    }

    /**
     * getter von "spiel"
     *
     * @return spiel
     */
    public boolean getSpiel() {
        return spiel;
    }

    /**
     * getter von "spielerSet"
     *
     * @return spielerSet
     */
    public HashSet getSpielerSet() {
        return spielerSet;
    }

    /**
     * getter von "spielTask"
     *
     * @return spielTask
     */
    public static int getSpielTask() {
        return spielTask;
    }

    /**
     * startet den Countdown bei dem spieler beitreten können
     */
    public void starteJoinCountdown() {
        sf.baueArena();
        joinCountdown = true;
        joinCountdownTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int counter = plugin.getConfig().getInt("Countdown-Zeit");

            @Override
            public void run() {
                counter--;
                if (counter == 0) {
                    joinCountdown = false;
                    if (spielerSet.size() > plugin.getConfig().getInt("Mindest-Spieler")) {
                        plugin.broadcastMessage("SnowSpleef startet jetzt");
                        plugin.getSpiel().starteStartCountdown();
                    } else {
                        plugin.broadcastMessage("Zu wenige Spieler haben SnowSpleef betreten");
                        plugin.getServer().getScheduler().cancelTask(sf.getBauTask());
                        for (String spieler : spielerSet) {
                            TerraWorld.removeSpieler(plugin.getServer().getPlayer(spieler));
                        }
                        spielerSet.clear();
                    }
                    plugin.getServer().getScheduler().cancelTask(joinCountdownTask);
                } else {
                    if (counter % 60 == 0) {
                        plugin.broadcastMessage("SnowSpleef startet in " + ChatColor.GOLD + (counter / 60) + ChatColor.GRAY + " Minuten");
                        plugin.getServer().broadcastMessage(ChatColor.GRAY + "Um beizutreten /tss beitreten eingeben");
                    }
                }
            }
        }, 0L, 20L);
    }

    /**
     * startet den Countdown bis zu dem die Spieler starten
     */
    public void starteStartCountdown() {

        final ItemStack[] items = new ItemStack[2];
        items[0] = new ItemStack(Material.IRON_SPADE, 1, (short) -32768);
        items[1] = new ItemStack(Material.SNOW_BALL, plugin.getConfig().getInt("Schneeball-Anzahl"));

        startCountdown = true;
        startCountdownTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int counter = plugin.getConfig().getInt("StartCountdown-Zeit") * 20;
            Iterator<String> it = spielerSet.iterator();
            Player spieler;

            @Override
            public void run() {
                counter--;
                if (counter % 20 == 0) {
                    plugin.broadcastMessage(ChatColor.GOLD + "" + (counter / 20) + "...");
                }
                if (it.hasNext()) {
                    spieler = plugin.getServer().getPlayer(it.next());
                    spieler.setGameMode(GameMode.SURVIVAL);
                    spieler.getInventory().clear();
                    spieler.getInventory().setContents(items);
                    spieler.teleport(plugin.getSpiel().getSpielfeld().zufaelligerSpawn());
                }
                if (counter == 0) {
                    plugin.broadcastMessage(ChatColor.GOLD + "GO!");
                    startCountdown = false;
                    plugin.getSpiel().starteSpiel();
                    plugin.getServer().getScheduler().cancelTask(startCountdownTask);
                }
            }
        }, 0L, 1L);
    }

    /**
     * startet das Spiel
     */
    public void starteSpiel() {
        spiel = true;
        spielTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            Iterator<String> it = spielerSet.iterator();
            Player spieler;
            Block currentblock;

            @Override
            public void run() {
                while (it.hasNext()) {
                    spieler = plugin.getServer().getPlayer(it.next());
                    if (sf.inSpielfeld(spieler.getLocation())) {
                        currentblock = spieler.getLocation().add(0, -1, 0).getBlock();
                        if (currentblock.getType() == Material.SNOW_BLOCK) {
                            currentblock.setTypeIdAndData(78, (byte) 6, false);
                        } else {
                            if (currentblock.getType() == Material.SNOW) {
                                if (currentblock.getData() == (byte) 1) {
                                    currentblock.setType(Material.AIR);
                                } else {
                                    currentblock.setData((byte) (currentblock.getData() - 1));
                                }
                            }
                        }
                    }
                }
                it = spielerSet.iterator();
            }
        }, 0L, 10L);
    }

    public Spielfeld getSpielfeld() {
        return sf;
    }
}
