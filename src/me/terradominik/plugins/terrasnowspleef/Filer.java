package me.terradominik.plugins.terrasnowspleef;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * die Filder Klasse, sie wird dafür verwendet,
 * die stats.yml zu Verwalten
 * @author Dominik
 */
public class Filer {

    private static YamlConfiguration stats;
    private static File statsFile;
    private static boolean loaded = false;
    private TerraSnowSpleef plugin;

    /**
     * der Konstruktor
     * @param plugin 
     */
    private Filer(TerraSnowSpleef plugin) {
        this.plugin = plugin;
    }

    /**
     * gibt die Config zurück
     * @return 
     */
    public static YamlConfiguration getConfig() {
        if (!loaded) {
            loadConfig();
        }
        return stats;
    }
    
    /**
     * gibt das File zurück
     * @return 
     */
    public static File getConfigFile() {
        return statsFile;
    }

    /**
     * ladet die Config
     */
    public static void loadConfig() {
        statsFile = new File(Bukkit.getServer().getPluginManager().getPlugin("TerraSnowSpleef").getDataFolder(), "stats.yml");
        if (statsFile.exists()) {
            stats = new YamlConfiguration();
            try {
                stats.load(statsFile);
            } catch (FileNotFoundException ex) {
            } catch (    IOException | InvalidConfigurationException ex) {
            }
            loaded = true;
        }
    }
    
    /**
     * speichert die Config
     * @throws IOException 
     */
    public static void saveConfig() throws IOException {
        stats.save(statsFile);
    }
}