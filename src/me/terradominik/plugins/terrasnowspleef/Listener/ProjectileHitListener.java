package me.terradominik.plugins.terrasnowspleef.Listener;

import me.terradominik.plugins.terrasnowspleef.TerraSnowSpleef;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
            Location loc = event.getEntity().getLocation();
            if (plugin.getSpiel().getSpielfeld().inSpielfeld(loc)) {
                for (int i = 0; i <= 4; i++) {
                    loc.add(0,-1,0);
                    loc.getBlock().setType(Material.AIR);
                    loc.getWorld().playEffect(loc, Effect.SMOKE, 10);
                }
            }
        }
    }
}
