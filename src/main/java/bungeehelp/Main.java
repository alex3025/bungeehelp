package bungeehelp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;


public class Main extends Plugin {
	public static Main plugin;
	
	public static List<String> updateNotification = new ArrayList<>();
	public static HashMap<String, Long> cooldowns = new HashMap<>();

	public void onEnable() {
		plugin = this;
		
		// Load configurations
		try {
			Settings.loadConfiguration("config.yml");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Check latest version and send update message to console
		if (Utils.checkLatestVersion() > Integer.parseInt(plugin.getDescription().getVersion().replace(".", ""))) {
			TextComponent message = new TextComponent(Utils.replaceColors(Settings.config.getString("messages.prefix")));
			message.addExtra(Utils.replaceColors(Settings.config.getString("messages.update-notification")));
			plugin.getProxy().getConsole().sendMessage(message);
		}

		// Enabling Commands
		getProxy().getPluginManager().registerCommand(plugin, new Commands());

		// Enabling Events
		getProxy().getPluginManager().registerListener(plugin, new Events());
	}

	public static Main getInstance() {
		return plugin;
	}
}
