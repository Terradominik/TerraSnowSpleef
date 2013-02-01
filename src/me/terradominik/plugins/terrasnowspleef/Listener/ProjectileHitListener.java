package me.terradominik.plugins.terrasnowspleef.Listener;

import me.terradominik.plugins.terrasnowspleef.TerraSnowSpleef;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ProjectileHitListener implements Listener {

    public TerraSnowSpleef plugin;

    public ProjectileHitListener(TerraSnowSpleef plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntityType() == EntityType.SNOWBALL) {
            if (plugin.getSpiel().getSpiel()) {
                Location loc = event.getEntity().getLocation();
                if (plugin.getSpiel().getSpielfeld().inSpielfeld(loc)) {
                    Block[] targetB = new Block[5];
                    targetB[0] = loc.getBlock().getRelative(BlockFace.UP);
                    for (int i = 0; i <= 5; i++) {
                        targetB[0] = targetB[0].getRelative(BlockFace.DOWN);
                        targetB[1] = targetB[0].getRelative(BlockFace.NORTH);
                        targetB[2] = targetB[0].getRelative(BlockFace.EAST);
                        targetB[3] = targetB[0].getRelative(BlockFace.SOUTH);
                        targetB[4] = targetB[0].getRelative(BlockFace.WEST);
                        for (Block b : targetB) {
                            if (b.getType() == Material.SNOW_BLOCK || b.getType() == Material.SNOW) {
                                b.getWorld().playEffect(loc, Effect.SMOKE, 10);
                                b.setType(Material.AIR);
                            }
                        }
                    }
                }
            }
        }
    }
}
