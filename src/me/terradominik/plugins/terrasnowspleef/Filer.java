package me.terradominik.plugins.terrasnowspleef;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class Filer {

    private static YamlConfiguration stats;
    private static File statsFile;
    private static boolean loaded = false;
    private TerraSnowSpleef plugin;

    private Filer(TerraSnowSpleef plugin) {
        this.plugin = plugin;
    }

    public static YamlConfiguration getConfig() {
        if (!loaded) {
            loadConfig();
        }
        return stats;
    }

    public static File getConfigFile() {
        return statsFile;
    }

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

    public static void saveConfig() throws IOException {
        stats.save(statsFile);
    }
}