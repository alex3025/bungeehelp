package bungeehelp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;


public class Settings {
	public static Main plugin = Main.getInstance();
	
	public static File configFile;
	public static Configuration config;

	public static void loadConfiguration(String configFilename) throws IOException {
		// Create plugin folder
		if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

		// Create/load config.yml
        configFile = new File(plugin.getDataFolder(), configFilename);
        
        if (!configFile.exists()) {
            InputStream in = plugin.getResourceAsStream(configFilename);
            Files.copy(in, configFile.toPath());
        }

		config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), configFilename));
	}
}
