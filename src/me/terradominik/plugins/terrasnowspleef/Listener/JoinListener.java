package me.terradominik.plugins.terrasnowspleef.Listener;

import com.tauncraft.tauncraftservermanager.TaunPlayer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.terradominik.plugins.terrasnowspleef.Filer;
import me.terradominik.plugins.terrasnowspleef.TerraSnowSpleef;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author Dominik
 */
public class JoinListener implements Listener {

    public TerraSnowSpleef plugin;
    
    public JoinListener(TerraSnowSpleef plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String name = event.getPlayer().getName();
        /**
        if(Filer.getConfig().getString(name+ ".GespielteRunden") != null && (Filer.getConfig().getBoolean(name + ".updatedToDB")==false)) {
            Filer.getConfig().set(name + ".updatedToDb", true);
            try {
                Filer.saveConfig();
            } catch (IOException ex) {
            }
            TaunPlayer tp = TaunPlayer.get(name);
            tp.addTaunpoints(Filer.getConfig().getInt(name + ".GespielteRunden"));
            tp.addTaunpoints(Filer.getConfig().getInt(name + ".GewonneneRunden")*50);
            System.out.println("Updated SnowSpleef Stats als Taunpoints für " + name);
        }*/
    }
}
