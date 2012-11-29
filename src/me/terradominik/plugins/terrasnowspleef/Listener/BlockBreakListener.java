package me.terradominik.plugins.terrasnowspleef.Listener;

import me.terradominik.plugins.terrasnowspleef.TerraSnowSpleef;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 *
 * @author Dominik
 */
public class BlockBreakListener implements Listener {

    public TerraSnowSpleef plugin;

    public BlockBreakListener(TerraSnowSpleef plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        try {
            if (event.getBlock().getWorld() == plugin.getServer().getWorld(plugin.getConfig().getString("Welt"))) {
                if (plugin.getSpiel().getSpielerSet().contains(event.getPlayer().getName())) {
                    if (plugin.getSpiel().getSpielfeld().inSpielfeld(event.getBlock().getLocation())) {
                        event.getBlock().getDrops().clear();
                    } else {
                        event.setCancelled(true);
                    }
                }

            }
        } catch (NullPointerException npe) {
        }
    }
}
