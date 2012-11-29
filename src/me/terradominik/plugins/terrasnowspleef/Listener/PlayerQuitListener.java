package me.terradominik.plugins.terrasnowspleef.Listener;

import me.terradominik.plugins.terrasnowspleef.Spiel;
import me.terradominik.plugins.terrasnowspleef.TerraSnowSpleef;
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
        plugin.getSpiel().getSpielerSet().remove(event.getPlayer().getName());
    }
}
