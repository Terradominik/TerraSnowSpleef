package me.terradominik.plugins.terrasnowspleef;

import java.util.HashSet;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;

/**
 * Die Spielfeld Klasse von TerraSnowSpleef
 *
 * @author Dominik
 */
public class Spielfeld {

    private TerraSnowSpleef plugin;
    private Location xPunkt;
    private Location yPunkt;
    private int ebenenAnzahl;
    private World welt;
    private HashSet<Integer> ebenen;
    private int bauTask;
    private int loeschTask;

    /**
     * Der Konstruktor von "Spielfeld"
     *
     * @param plugin
     */
    public Spielfeld(TerraSnowSpleef plugin) {
        this.plugin = plugin;

        plugin.getServer().createWorld(new WorldCreator(plugin.getConfig().getString("Welt")));
        welt = plugin.getServer().getWorld(plugin.getConfig().getString("Welt"));

        String[] zwischen = plugin.getConfig().getString("xPunkt").split(",");
        xPunkt = new Location(
                welt,
                Integer.parseInt(zwischen[0]),
                Integer.parseInt(zwischen[1]),
                Integer.parseInt(zwischen[2]));

        zwischen = plugin.getConfig().getString("yPunkt").split(",");
        yPunkt = new Location(
                welt,
                Integer.parseInt(zwischen[0]),
                Integer.parseInt(zwischen[1]),
                Integer.parseInt(zwischen[2]));

        ebenenAnzahl = plugin.getConfig().getInt("Ebenen-Anzahl");
        ebenen = new HashSet<>();
        for (int i = 0; i < ebenenAnzahl; i++) {
            ebenen.add(xPunkt.getBlockY() - (i * 20));
        }
    }

    public Location zufaelligerSpawn() {
        return new Location(
                welt,
                xPunkt.getX() + ((int) (Math.random() * (yPunkt.getX() - xPunkt.getX() + 1))),
                xPunkt.getY(),
                xPunkt.getZ() + ((int) (Math.random() * (yPunkt.getZ() - xPunkt.getZ() + 1))));
    }

    public void baueArena() {
        bauTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int ycounter = xPunkt.getBlockY();
            int i1 = 0;
            int i2 = 0;
            int i3 = 0;

            @Override
            public void run() {
                if (i1 < ebenenAnzahl) {
                    if (i2 <= Math.abs(yPunkt.getBlockX() - xPunkt.getBlockX())) {
                        for (int i3 = 0; i3 < 5; i3++) {
                            for (int i4 = 0; i4 <= Math.abs(yPunkt.getBlockZ() - xPunkt.getBlockZ()); i4++) {
                                welt.getBlockAt(
                                        xPunkt.getBlockX() + (i2 * (yPunkt.getBlockX() / Math.abs(yPunkt.getBlockX()))),
                                        ycounter - i3,
                                        xPunkt.getBlockZ() + (i4 * (yPunkt.getBlockZ() / Math.abs(yPunkt.getBlockZ())))
                                ).setType(Material.SNOW_BLOCK);
                            }
                        }
                        i2++;
                    } else {
                        i2 = 0;
                        i1++;
                        ycounter -= 20;
                    }
                } else {
                    plugin.getServer().getScheduler().cancelTask(bauTask);
                }
            }
        }, 0L, 5L);
    }

    public void baueArenaAlt() {
        int ycounter = xPunkt.getBlockY();
        for (int i1 = 0; i1 < ebenenAnzahl; i1++) {
            for (int i2 = 0; i2 < 5; i2++) {
                for (int i3 = 0; i3 <= Math.abs(yPunkt.getBlockX() - xPunkt.getBlockX()); i3++) {
                    for (int i4 = 0; i4 <= Math.abs(yPunkt.getBlockZ() - xPunkt.getBlockZ()); i4++) {
                        welt.getBlockAt(xPunkt.getBlockX() + (i3 * (yPunkt.getBlockX() / Math.abs(yPunkt.getBlockX()))), ycounter, xPunkt.getBlockZ() + (i4 * (yPunkt.getBlockZ() / Math.abs(yPunkt.getBlockZ())))).setType(Material.SNOW_BLOCK);
                    }
                }
                ycounter--;
            }
            ycounter -= 15;
        }
    }

    public boolean inSpielfeld(Location loc) {
        boolean ausgabe = false;
        if (loc.getX() <= xPunkt.getX()
                && loc.getX() >= yPunkt.getX()
                && loc.getZ() <= xPunkt.getZ()
                && loc.getZ() >= yPunkt.getZ()
                && loc.getY() <= xPunkt.getY()) {
            ausgabe = true;
        }
        return ausgabe;
    }

    public void loescheFelder(int y) {
        Iterator<String> itP = plugin.getSpiel().getSpielerSet().iterator();
        boolean istOberster = true;
        while (itP.hasNext()) {
            if (plugin.getServer().getPlayer(itP.next()).getLocation().getBlockY() > y) {
                istOberster = false;
            }
        }
        int zwischen;
        if (istOberster) {
            Iterator<Integer> itI = ebenen.iterator();
            while (itI.hasNext()) {
                zwischen = itI.next();
                if (y < zwischen) {
                    this.loescheEbene(zwischen);
                    ebenen.remove(zwischen);
                }
            }
        }
    }

    public void loescheEbene(final int y) {
        loeschTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int ycounter = xPunkt.getBlockY();
            int i2 = 0;

            @Override
            public void run() {
                if (i2 <= Math.abs(yPunkt.getBlockX() - xPunkt.getBlockX())) {
                    for (int i3 = 0; i3 < 5; i3++) {
                        for (int i4 = 0; i4 <= Math.abs(yPunkt.getBlockZ() - xPunkt.getBlockZ()); i4++) {
                            welt.getBlockAt(xPunkt.getBlockX() + (i3 * (yPunkt.getBlockX() / Math.abs(yPunkt.getBlockX()))), ycounter, xPunkt.getBlockZ() + (i4 * (yPunkt.getBlockZ() / Math.abs(yPunkt.getBlockZ())))).setType(Material.SNOW_BLOCK);
                        }
                        ycounter--;
                    }
                } else {
                    plugin.getServer().getScheduler().cancelTask(loeschTask);
                }
            }
        }, 0L, 5L);
    }

    public int getBauTask() {
        return bauTask;
    }
}
