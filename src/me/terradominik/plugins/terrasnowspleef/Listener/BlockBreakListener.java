package me.terradominik.plugins.terrasnowspleef.Listener;

import me.terradominik.plugins.terrasnowspleef.TerraSnowSpleef;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Dominik
 */
public class BlockBreakListener implements Listener {

    public TerraSnowSpleef plugin;
    public ItemStack hand = new ItemStack(Material.AIR);
    
    public BlockBreakListener(TerraSnowSpleef plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        try {
            if (event.getBlock().getWorld() == plugin.getServer().getWorld(plugin.getConfig().getString("Welt"))) {
                if (plugin.getSpiel().getSpielerSet().contains(event.getPlayer().getName())) {
                    if (plugin.getSpiel().getSpiel()) {
                        Block block = event.getBlock();
                        if (plugin.getSpiel().getSpielfeld().inSpielfeld(block.getLocation())) {
                            block.breakNaturally(hand);
                        } else {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }

            }
        } catch (NullPointerException npe) {
        }
    }
}
