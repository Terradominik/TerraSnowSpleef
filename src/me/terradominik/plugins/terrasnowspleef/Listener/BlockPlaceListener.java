package me.terradominik.plugins.terrasnowspleef.Listener;

import me.terradominik.plugins.terrasnowspleef.TerraSnowSpleef;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 *
 * @author Dominik
 */
public class BlockPlaceListener implements Listener {

    public TerraSnowSpleef plugin;

    public BlockPlaceListener(TerraSnowSpleef plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        try {
        if (event.getBlock().getWorld() == plugin.getServer().getWorld(plugin.getConfig().getString("Welt"))) {
            if (plugin.getSpiel().getSpiel()) {
                if (plugin.getSpiel().getSpielfeld().inSpielfeld(event.getBlock().getLocation())) {
                    if (plugin.getSpiel().getSpielerSet().contains(event.getPlayer().getName())) event.setCancelled(true);
                }
            }
        }
        } catch (NullPointerException npe) {
            
        }
    }
}
