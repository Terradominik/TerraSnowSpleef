package me.terradominik.plugins.terrasnowspleef.Listener;

import me.terradominik.plugins.terrasnowspleef.TerraSnowSpleef;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    
    public TerraSnowSpleef plugin;
    
     public PlayerJoinListener(TerraSnowSpleef plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(plugin.getConfig().getBoolean("config")) {
            String[] loc = plugin.getConfig().getString("event.spawn").split(",");
            event.getPlayer().teleport(
                    new Location(
                        plugin.getServer().getWorld(loc[0]),
                        Integer.parseInt(loc[1]),
                        Integer.parseInt(loc[2]),
                        Integer.parseInt(loc[3]),
                        Float.parseFloat(loc[4]),
                        Float.parseFloat(loc[5])
                    )
           );
        }
    }
}
