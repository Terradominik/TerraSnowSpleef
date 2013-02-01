package me.terradominik.plugins.terrasnowspleef.Listener;

import me.terradominik.plugins.terrasnowspleef.TerraSnowSpleef;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerToggleSneakListener implements Listener {

    public TerraSnowSpleef plugin;

    public PlayerToggleSneakListener(TerraSnowSpleef plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        if (event.isSneaking() == true) {
            final Player spieler = event.getPlayer();
            if (spieler.getWorld() == plugin.getServer().getWorld(plugin.getConfig().getString("Welt"))
                    && plugin.getSpiel().getSpielerSet().contains(spieler.getName())
                    && spieler.getLocation().add(0, 2, 0).getBlock().getType() == Material.SNOW_BLOCK) {
                BukkitRunnable toggleTask = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(spieler.isSneaking()) {
                            spieler.getLocation().add(0, -1, 0).getBlock().setType(Material.AIR);
                        }
                    }
                };
                toggleTask.runTaskLater(plugin, 600L);
            }
        }
    }
}
