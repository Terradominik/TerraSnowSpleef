package me.terradominik.plugins.terrasnowspleef.Listener;

import com.tauncraft.tauncraftservermanager.SpielerListe;
import com.tauncraft.tauncraftservermanager.TaunPlayer;
import java.util.HashSet;
import java.util.Iterator;
import me.terradominik.plugins.terrasnowspleef.Filer;
import me.terradominik.plugins.terrasnowspleef.TerraSnowSpleef;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
    public Location totspawn;

    public EntityDamageListener(TerraSnowSpleef plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        try {
            if (event.getEntity() instanceof Player && event.getEntity().getWorld() == plugin.getServer().getWorld(plugin.getConfig().getString("Welt"))) {
                if (plugin.getSpiel().getSpiel()) {
                    if (event.getCause() == DamageCause.FALL) {
                        event.setDamage(0);
                        if (plugin.getSpiel().getSpielerSet().contains(((Player) event.getEntity()).getName())) {
                            int y = event.getEntity().getLocation().getBlockY();
                            if (y < plugin.getConfig().getInt("Boden")) {
                                Player spieler = (Player) event.getEntity();

                                Filer.getConfig().set(spieler.getName() + ".GespielteRunden", Integer.parseInt(Filer.getConfig().getString(spieler.getName() + ".GespielteRunden")) + 1);
                                TaunPlayer.get(spieler).addTaunpoints(1);

                                HashSet<String> set = plugin.getSpiel().getSpielerSet();
                                set.remove(spieler.getName());
                                plugin.broadcastMessage(spieler.getName() + " ist runtergefallen, es sind noch " + ChatColor.GOLD + "" + set.size() + ChatColor.GRAY + " Spieler übrig");
                                plugin.getSpiel().setSpielerSet(set);
                                String[] temp = plugin.getConfig().getString("TotSpawn").split(",");
                                totspawn = new Location(
                                        plugin.getServer().getWorld(temp[0]),
                                        Integer.parseInt(temp[1]),
                                        Integer.parseInt(temp[2]),
                                        Integer.parseInt(temp[3]));
                                spieler.teleport(totspawn);
                                spieler.getInventory().clear();


                                SpielerListe.remove(spieler);
                                if (set.size() == 1) {
                                    Iterator<String> it = set.iterator();
                                    spieler = plugin.getServer().getPlayer(it.next());
                                    plugin.broadcastMessage(ChatColor.GOLD + spieler.getName() + ChatColor.GRAY + " hat gewonnen!");

                                    //Update Statistik
                                    Filer.getConfig().set(spieler.getName() + ".GespielteRunden", Integer.parseInt(Filer.getConfig().getString(spieler.getName() + ".GespielteRunden")) + 1);
                                    Filer.getConfig().set(spieler.getName() + ".GewonneneRunden", Integer.parseInt(Filer.getConfig().getString(spieler.getName() + ".GewonneneRunden")) + 1);
                                    TaunPlayer.get(spieler).addTaunpoints(51);

                                    set.remove(spieler.getName());
                                    plugin.getSpiel().setSpielerSet(set);
                                    SpielerListe.remove(spieler);
                                    spieler.teleport(totspawn);
                                    spieler.getInventory().clear();
                                    plugin.neuesSpiel();
                                }
                            } else {
                                //plugin.getSpiel().getSpielfeld().loescheFelder(event.getEntity().getLocation().getBlockY());
                            }
                        }
                    }
                    event.setDamage(0);
                }
            }
        } catch (NullPointerException npe) {
        }
    }
}
