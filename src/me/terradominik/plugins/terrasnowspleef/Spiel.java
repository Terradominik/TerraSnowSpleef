package me.terradominik.plugins.terrasnowspleef;

import java.util.HashSet;
import java.util.Iterator;
import me.terradominik.plugins.terraworld.TerraWorld;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * die Spiellogik-Klasse von TerraSnowSpleef
 * @author Dominik
 */
public class Spiel {

    private TerraSnowSpleef plugin;
    private boolean joinCountdown; //ist true wenn der countdown zum beitreten des Spieles läuft
    private boolean startCountdown; //ist true wenn der countdown zum starten des Spieles läuft
    private boolean spiel; //ist true wenn ein spiel läuft
    private HashSet<String> spielerSet = new HashSet<>();
    public static Spielfeld sf;

    /**
     * Der Konstruktor von "Spiel"
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
     * @return joinCountdown
     */
    public boolean getJoinCountdown() {
        return joinCountdown;
    }

    /**
     * getter von "startCountdown"
     * @return startCountdown
     */
    public boolean getStartCountdown() {
        return startCountdown;
    }

    /**
     * getter von "spiel"
     * @return spiel
     */
    public boolean getSpiel() {
        return spiel;
    }

    /**
     * getter von "spielerSet"
     * @return spielerSet
     */
    public HashSet<String> getSpielerSet() {
        return spielerSet;
    }
    
    /**
     * getter von "sf"
     * @return sf
     */
    public Spielfeld getSpielfeld() {
        return sf;
    }

    /**
     * startet den Countdown bei dem Spieler beitreten können
     */
    public void starteJoinCountdown() {
        sf.baueArena();
        joinCountdown = true;
        BukkitRunnable joinCountdownTask = new BukkitRunnable() {
            int counter = plugin.getConfig().getInt("Countdown-Zeit");

            @Override
            public void run() {
                if (counter == 0) {
                    joinCountdown = false;
                    if (spielerSet.size() >= plugin.getConfig().getInt("Mindest-Spieler")) {
                        plugin.broadcastMessage("SnowSpleef startet jetzt");
                        plugin.getSpiel().starteStartCountdown();
                    } else {
                        plugin.broadcastMessage("Zu wenige Spieler haben SnowSpleef betreten");
                        sf.getBauArenaTask().cancel();
                        for (String spieler : spielerSet) {
                            TerraWorld.removeSpieler(plugin.getServer().getPlayer(spieler));
                        }
                        spielerSet.clear();
                    }
                    this.cancel();
                } else {
                    if (counter % 60 == 0) {
                        plugin.broadcastMessage("SnowSpleef startet in " + ChatColor.GOLD + (counter / 60) + ChatColor.GRAY + " Minuten");
                        plugin.getServer().broadcastMessage(ChatColor.GRAY + "Um beizutreten /tss beitreten eingeben");
                    }
                }
                counter--;
            }
        };
        joinCountdownTask.runTaskTimer(plugin, 0L, 20L);
    }

    /**
     * startet den Countdown bis zu dem das Spiel startet
     */
    public void starteStartCountdown() {

        final ItemStack[] items = new ItemStack[2];
        items[0] = new ItemStack(Material.IRON_SPADE, 1, (short) -32768);
        items[1] = new ItemStack(Material.SNOW_BALL, plugin.getConfig().getInt("Schneeball-Anzahl"));

        startCountdown = true;
        BukkitRunnable startCountdownTask = new BukkitRunnable() {
            int counter = plugin.getConfig().getInt("StartCountdown-Zeit") * 20;
            Iterator<String> it = spielerSet.iterator();
            Player spieler;

            @Override
            public void run() {
                counter--;
                if (counter == 0) {
                    plugin.broadcastMessage(ChatColor.GOLD + "GO!");
                    startCountdown = false;
                    plugin.getSpiel().starteSpiel();
                    this.cancel();
                } else {
                    if (counter % 20 == 0) {
                        plugin.broadcastMessage(ChatColor.GOLD + "" + (counter / 20) + "...");
                    }
                }
                if (it.hasNext()) {
                    spieler = plugin.getServer().getPlayer(it.next());
                    spieler.setGameMode(GameMode.SURVIVAL);
                    spieler.getInventory().clear();
                    spieler.getInventory().setContents(items);
                    spieler.teleport(plugin.getSpiel().getSpielfeld().zufaelligerSpawn());
                }
            }
        };
        startCountdownTask.runTaskTimer(plugin, 0L, 1L);
    }

    /**
     * startet das Spiel
     */
    public void starteSpiel() {
        spiel = true;
    }
}
