package bungeehelp;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.ArrayList;
import java.util.List;

public class Events implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST)
    public void ChatEvent(ChatEvent event) {
		if (event.isCommand()) {
			ProxiedPlayer player = (ProxiedPlayer) event.getSender();
			String serverName = player.getServer().getInfo().getName();
			String playerCommand = event.getMessage().replace("/", "");
			
			List<String> serverCustomAliases;
			try {
				serverCustomAliases = Settings.config.getStringList("servers." + serverName + ".aliases");
			} catch (Exception e) {
				serverCustomAliases = new ArrayList<>();
			}

			if (((Utils.containsCaseInsensitive(playerCommand, serverCustomAliases) || Utils.containsCaseInsensitive(playerCommand, Settings.config.getStringList("globalAliases"))) && !Settings.config.getStringList("serversBlacklist").contains(serverName)) && player.hasPermission("bungeehelp.view")) {
				event.setCancelled(true);
				try {
					if (Utils.notOnCooldown(player, false, serverName)) {
						for (String line : Settings.config.getStringList("servers." + serverName + ".help")) {
							player.sendMessage(new TextComponent(Utils.replaceColors(line)));
						}
						Main.cooldowns.put(player.getUniqueId().toString(), System.currentTimeMillis());
					}
				} catch (Exception e) {
					if (Utils.notOnCooldown(player, true, null)) {
						for (String line : Settings.config.getStringList("globalHelp.help")) {
							player.sendMessage(new TextComponent(Utils.replaceColors(line)));
						}
						Main.cooldowns.put(player.getUniqueId().toString(), System.currentTimeMillis());
					}
				}
				event.setCancelled(true);
			}
		}
    }

	@EventHandler
	public void PostLoginEvent(PostLoginEvent event) {
		ProxiedPlayer player = event.getPlayer();
		Main plugin = Main.getInstance();
		
		// Update notifier
		if (Utils.checkLatestVersion() > Integer.parseInt(plugin.getDescription().getVersion().replace(".", ""))) {
			if (player.hasPermission("bungeehelp.updatenotify") && !Main.updateNotification.contains(player.getUniqueId().toString())) {
				TextComponent message = new TextComponent(Utils.replaceColors(Settings.config.getString("messages.prefix")));
				message.addExtra(Utils.replaceColors(Settings.config.getString("messages.update-notification")));
				plugin.getProxy().getConsole().sendMessage(message);
				player.sendMessage(message);
				Main.updateNotification.add(player.getUniqueId().toString());
			}
		}
	}
}
