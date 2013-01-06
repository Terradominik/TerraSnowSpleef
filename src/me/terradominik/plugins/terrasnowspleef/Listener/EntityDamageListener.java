package me.terradominik.plugins.terrasnowspleef.Listener;

import java.util.HashSet;
import java.util.Iterator;
import me.terradominik.plugins.terrasnowspleef.Filer;
import me.terradominik.plugins.terrasnowspleef.TerraSnowSpleef;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 *
 * @author Dominik
 */
public class EntityDamageListener implements Listener {

    public TerraSnowSpleef plugin;

    public EntityDamageListener(TerraSnowSpleef plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        try {
            if (event.getEntity() instanceof Player && event.getEntity().getWorld() == plugin.getServer().getWorld(plugin.getConfig().getString("Welt"))) {
                if (plugin.getSpiel().getSpiel()) {
                    if (plugin.getSpiel().getSpielfeld().inSpielfeld(event.getEntity().getLocation())) {
                        event.setDamage(0);
                        if (event.getCause() == DamageCause.FALL) {
                            int y = event.getEntity().getLocation().getBlockY();

                            plugin.getSpiel().getSpielfeld().loescheFelder(event.getEntity().getLocation().getBlockY());
                            if (y < plugin.getConfig().getInt("Boden")) {
                                HashSet<String> set = (HashSet<String>) plugin.getSpiel().getSpielerSet().clone();
                                Player spieler = (Player) event.getEntity();
                                Filer.getConfig().set(spieler.getName() + ".GespielteRunden", Integer.parseInt(Filer.getConfig().getString(spieler.getName() + ".GespielteRunden")) + 1);
                                set.remove(spieler.getName());
                                String[] arrayLoc = plugin.getConfig().getString("totspawn").split(",");
                                Location loc =
                                        new Location(
                                        plugin.getServer().getWorld(arrayLoc[0]),
                                        Integer.parseInt(arrayLoc[1]),
                                        Integer.parseInt(arrayLoc[2]),
                                        Integer.parseInt(arrayLoc[3]),
                                        Float.parseFloat(arrayLoc[4]),
                                        Float.parseFloat(arrayLoc[5]));
                                spieler.teleport(loc);
                                if (set.size() == 1) {
                                    Iterator<String> it = set.iterator();
                                    spieler = plugin.getServer().getPlayer(it.next());
                                    plugin.broadcastMessage(ChatColor.GOLD + spieler.getName() + ChatColor.GRAY + " hat gewonnen!");

                                    //Update Statistik
                                    Filer.getConfig().set(spieler.getName() + ".GespielteRunden", Integer.parseInt(Filer.getConfig().getString(spieler.getName() + ".GespielteRunden")) + 1);
                                    Filer.getConfig().set(spieler.getName() + ".GewonneneRunden", Integer.parseInt(Filer.getConfig().getString(spieler.getName() + ".GewonneneRunden")) + 1);

                                    set.remove(spieler.getName());
                                    spieler.teleport(loc);

                                    plugin.neuesSpiel();
                                } else {
                                    plugin.broadcastMessage(spieler.getName() + " ist runtergefallen, es sind noch " + ChatColor.GOLD + "" + set.size() + ChatColor.GRAY + " Spieler übrig");
                                }
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException npe) {
        }
    }
}
