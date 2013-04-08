package me.terradominik.plugins.terrasnowspleef.Listener;

import java.util.HashSet;
import java.util.Iterator;
import me.terradominik.plugins.terrasnowspleef.Filer;
import me.terradominik.plugins.terrasnowspleef.TerraSnowSpleef;
import me.terradominik.plugins.terraworld.TerraWorld;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    public TerraSnowSpleef plugin;

    public PlayerQuitListener(TerraSnowSpleef plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        try {
            if (plugin.getSpiel().getSpielerSet().remove(event.getPlayer().getName())) {
                event.getPlayer().setHealth(0);
            }
            if (plugin.getSpiel().getSpielerSet().size() == 1 && plugin.getSpiel().getSpiel()) {
                HashSet<String> set = plugin.getSpiel().getSpielerSet();
                Iterator<String> it = plugin.getSpiel().getSpielerSet().iterator();
                Player spieler = plugin.getServer().getPlayer(it.next());
                plugin.broadcastMessage(ChatColor.GOLD + spieler.getName() + ChatColor.GRAY + " hat gewonnen!");

                //Update Statistik
                Filer.getConfig().set(spieler.getName() + ".GespielteRunden", Integer.parseInt(Filer.getConfig().getString(spieler.getName() + ".GespielteRunden")) + 1);
                Filer.getConfig().set(spieler.getName() + ".GewonneneRunden", Integer.parseInt(Filer.getConfig().getString(spieler.getName() + ".GewonneneRunden")) + 1);

                plugin.getSpiel().getSpielerSet().remove(spieler.getName());
                plugin.getSpiel().setSpielerSet(set);
                TerraWorld.removeSpieler(spieler);
                String[] temp = plugin.getConfig().getString("TotSpawn").split(",");
                Location totspawn = new Location(
                        plugin.getServer().getWorld(temp[0]),
                        Integer.parseInt(temp[1]),
                        Integer.parseInt(temp[2]),
                        Integer.parseInt(temp[3]));
                spieler.teleport(totspawn);
                spieler.getInventory().clear();
                plugin.neuesSpiel();
            }
        } catch (NullPointerException npe) {
        }
    }
}
