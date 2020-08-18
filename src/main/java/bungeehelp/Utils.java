package bungeehelp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.PluginDescription;


public class Utils {
	// Convert color codes "§" to "&".
	public static String replaceColors(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	// Check if an array contain a case insensitive string.
	public static boolean containsCaseInsensitive(String s, List<String> l) {
        return l.stream().anyMatch(x -> x.equalsIgnoreCase(s));
    }
	
	// Check if a player isn't on command cooldown.
	public static boolean notOnCooldown(ProxiedPlayer player, Boolean isGlobal, String serverName) {
		if (Main.cooldowns.containsKey(player.getUniqueId().toString())) {
			int cooldown;
			if (isGlobal) {
				cooldown = Settings.config.getInt("globalHelp.cooldown");
			} else {
				cooldown = Settings.config.getInt("servers." + serverName + ".cooldown");
			}
			long secondsLeft = ((Main.cooldowns.get(player.getUniqueId().toString()) / 1000) + cooldown) - (System.currentTimeMillis() / 1000);
            if (secondsLeft > 0 && !player.hasPermission("bungeehelp.cooldown.bypass")) {
                player.sendMessage(new TextComponent(Utils.replaceColors(Settings.config.getString("messages.on-cooldown").replace("%seconds%", Long.toString(secondsLeft)))));
                return false;
            }
        }
		return true;
	}

	// Get the latest version of the plugin.
	public static Integer checkLatestVersion() {
		Main plugin = Main.getInstance();
		
		String USER_AGENT  = "BungeeHelp";
		String REQUEST_URL = "https://api.spiget.org/v2/resources/75583/versions?size=1&sort=-name&fields=name";
		PluginDescription pdf = plugin.getDescription();

		try {
			URL url = new URL(REQUEST_URL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.addRequestProperty("User-Agent", USER_AGENT);

			InputStream inputStream = connection.getInputStream();
			InputStreamReader reader = new InputStreamReader(inputStream);

			JsonElement element = new JsonParser().parse(reader);

			return Integer.parseInt(element.getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString().replace(".", ""));
		} catch (IOException e) {
			TextComponent errorMessage = new TextComponent(Utils.replaceColors(Settings.config.getString("messages.prefix")));
			errorMessage.addExtra(Utils.replaceColors(Settings.config.getString("messages.console-check-version-error")));
			plugin.getProxy().getConsole().sendMessage(errorMessage);
		}
		return Integer.parseInt(pdf.getVersion().replace(".", ""));
	}
}
