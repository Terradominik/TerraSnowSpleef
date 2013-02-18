package me.terradominik.plugins.terrasnowspleef;

import java.util.ArrayList;
import java.util.LinkedList;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.scheduler.BukkitRunnable;

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
    private ArrayList<Integer> ebenenSpieler = new ArrayList<>();

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
    }

    public Location zufaelligerSpawn() {
        return new Location(
                welt,
                xPunkt.getX() + ((int) (Math.random() * (yPunkt.getX() - xPunkt.getX() + 1))),
                xPunkt.getY() + 1,
                xPunkt.getZ() + ((int) (Math.random() * (yPunkt.getZ() - xPunkt.getZ() + 1))));
    }

    public void baueArena() {
        plugin.getServer().broadcastMessage(ChatColor.GRAY + "Die Arena beginnt sich aufzubauen");
        BukkitRunnable bauArenaTask = new BukkitRunnable() {
            int ycounter = xPunkt.getBlockY();
            int i2 = 0;

            @Override
            public void run() {

                if (i2 < Math.abs(yPunkt.getBlockX() - xPunkt.getBlockX())) {
                    for (int i4 = 0; i4 <= Math.abs(yPunkt.getBlockZ() - xPunkt.getBlockZ()); i4++) {
                        for (int i1 = 0; i1 < ebenenAnzahl; i1++) {
                            for (int i3 = 0; i3 < 5; i3++) {
                                welt.getBlockAt(
                                        xPunkt.getBlockX() + (i2 * (yPunkt.getBlockX() / Math.abs(yPunkt.getBlockX()))),
                                        ycounter - i3 - (i1 * 20),
                                        xPunkt.getBlockZ() + (i4 * (yPunkt.getBlockZ() / Math.abs(yPunkt.getBlockZ())))).setType(Material.SNOW_BLOCK);
                            }
                        }
                    }
                    i2++;
                } else {
                    Spielfeld.this.secureClose();
                    this.cancel();
                }

            }
        };
        bauArenaTask.runTaskTimer(plugin, 0L, 5L);
    }

    public void secureClose() {
        BukkitRunnable secureCloseTask = new BukkitRunnable() {
            int ycounter = xPunkt.getBlockY();
            int i2 = Math.abs(yPunkt.getBlockX() - xPunkt.getBlockX());
            int i4 = 0;

            @Override
            public void run() {
                if (i4 <= Math.abs(yPunkt.getBlockZ() - xPunkt.getBlockZ())) {
                    for (int i1 = ebenenAnzahl - 1; i1 >= 0; i1--) {
                        for (int i3 = 4; i3 >= 0; i3--) {
                            welt.getBlockAt(
                                    xPunkt.getBlockX() + (i2 * (yPunkt.getBlockX() / Math.abs(yPunkt.getBlockX()))),
                                    ycounter - i3 - (i1 * 20),
                                    xPunkt.getBlockZ() + (i4 * (yPunkt.getBlockZ() / Math.abs(yPunkt.getBlockZ())))).setType(Material.SNOW_BLOCK);
                        }

                    }
                    i4++;
                } else {
                    plugin.getServer().broadcastMessage(ChatColor.GRAY + "Die Arena hat sich fertig aufgebaut");
                    this.cancel();
                }

            }
        };
        secureCloseTask.runTaskTimer(plugin, 0L, 5L);
    }

    public boolean inSpielfeld(Location loc) {
        if (loc.getX() <= xPunkt.getX() + 1
                && loc.getX() >= yPunkt.getX()
                && loc.getZ() <= xPunkt.getZ() + 1
                && loc.getZ() >= yPunkt.getZ()
                && loc.getY() <= xPunkt.getY() + 7
                && loc.getY() >= xPunkt.getY() - 85) {
            return true;
        }
        return false;
    }

    public void loescheEbene(int id) {
        plugin.getServer().broadcastMessage(ChatColor.GRAY + "Ebene " + (id + 1) + " wird in 60 Sekunden gelöscht");
        final int ycounter = this.getEbenenY(id);
        BukkitRunnable loescheEbeneTask = new BukkitRunnable() {
            int i2 = 0;

            @Override
            public void run() {

                if (i2 <= Math.abs(yPunkt.getBlockX() - xPunkt.getBlockX())) {
                    for (int i4 = 0; i4 <= Math.abs(yPunkt.getBlockZ() - xPunkt.getBlockZ()); i4++) {
                        for (int i3 = 0; i3 < 5; i3++) {
                            welt.getBlockAt(
                                    xPunkt.getBlockX() + (i2 * (yPunkt.getBlockX() / Math.abs(yPunkt.getBlockX()))),
                                    ycounter - i3,
                                    xPunkt.getBlockZ() + (i4 * (yPunkt.getBlockZ() / Math.abs(yPunkt.getBlockZ())))).setType(Material.AIR);
                        }
                    }
                    i2++;
                } else {
                    this.cancel();
                }
            }
        };
        loescheEbeneTask.runTaskTimer(plugin, 1200L, 10L);
    }

    public int getEbenenY(int id) {
        return (xPunkt.getBlockY() - (id * 20));
    }

    public int getEbenenID(int i) {
        return ((-i + 1 + xPunkt.getBlockY()) / 20);
    }

    public void ebenenAbbau() {
        int zeit = (int) Math.log(Math.pow(plugin.getSpiel().getSpielerSet().size(), 3));
        zeit = (zeit - 1) * 60 * 20;
        BukkitRunnable ebenenAbbauTask = new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (i < 4) {
                    Spielfeld.this.loescheEbene(i);
                    i++;
                } else {
                    this.cancel();
                }
            }
        };
        ebenenAbbauTask.runTaskTimer(plugin, (long) zeit, (long) zeit);
    }

    public boolean inSpielfeldBlock(Location loc) {
        if (loc.getX() <= xPunkt.getX()
                && loc.getX() >= yPunkt.getX()
                && loc.getZ() <= xPunkt.getZ()
                && loc.getZ() >= yPunkt.getZ()
                && loc.getY() <= xPunkt.getY() + 7
                && loc.getY() >= xPunkt.getY() - 85) {
            return true;
        }
        return false;
    }
}
