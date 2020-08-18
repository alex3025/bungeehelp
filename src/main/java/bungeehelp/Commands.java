package bungeehelp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.lang.String;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.PluginDescription;
import net.md_5.bungee.api.plugin.TabExecutor;


public class Commands extends Command implements TabExecutor {
	public static Main plugin = Main.getInstance();

	public Commands() {
		super("bungeehelp", "", "bh");
	}

    @Override
    public void execute(CommandSender commandSender, String[] args) {
    	TextComponent mainMessage = new TextComponent(Utils.replaceColors(Settings.config.getString("messages.prefix")));
		if (args.length == 0) {
			PluginDescription pdf = plugin.getDescription();

			commandSender.sendMessage(new TextComponent("§8§m+------------------+§e§l BungeeHelp §8§m+------------------+"));
    		commandSender.sendMessage();
    		commandSender.sendMessage(new TextComponent("    §f" + pdf.getDescription()));
    		commandSender.sendMessage();

    		TextComponent author = new TextComponent("    §7Author: ");
    		TextComponent authorHover = new TextComponent("§aalex3025");
    		authorHover.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/members/alex3025.495945/"));
    		authorHover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§fVisit my profile on SpigotMC!")));
    		author.addExtra(authorHover);
    		commandSender.sendMessage(author);
    		
    		if (commandSender.hasPermission("bungeehelp.updatenotify")) {
        		TextComponent version = new TextComponent("    §7Version: ");
				TextComponent versionHover = new TextComponent();
				String color;

        		if (Utils.checkLatestVersion() > Integer.parseInt(pdf.getVersion().replace(".", ""))) {
					color = "§c";
        			versionHover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§cYou're not using the latest version!\nPlease update the plugin.")));
        		} else {
					color = "§a";
        			versionHover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aYou're using the latest version!")));
        		}

        		versionHover.addExtra(color + pdf.getVersion());
				version.addExtra(versionHover);
        		commandSender.sendMessage(version);
    		}

    		commandSender.sendMessage();
    		commandSender.sendMessage(new TextComponent("§8§m+------------------+§e§l BungeeHelp §8§m+------------------+"));

    	} else if (args[0].equalsIgnoreCase("help")) {
    		if (commandSender.hasPermission("bungeehelp.help")) {
        		commandSender.sendMessage(new TextComponent("§8§m+------------------+§e§l BungeeHelp §8§m+------------------+"));
    			commandSender.sendMessage();
    			
    			TextComponent cmd1 = new TextComponent("    ");
    			TextComponent cmd1Perms = new TextComponent("§7§l· §a/bungeehelp");
    			cmd1Perms.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/bungeehelp"));
    			cmd1.addExtra(cmd1Perms);
    			cmd1.addExtra("§8 - ".concat(Utils.replaceColors(Settings.config.getString("messages.commands.self.description"))));
    			commandSender.sendMessage(cmd1);
    			
    			TextComponent cmd2 = new TextComponent("    ");
    			TextComponent cmd2Perms = new TextComponent("§7§l· §a/bh help");
    			cmd2Perms.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§fbungeehelp.help")));
    			cmd2Perms.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/bh help"));
    			cmd2.addExtra(cmd2Perms);
    			cmd2.addExtra("§8 - ".concat(Utils.replaceColors(Settings.config.getString("messages.commands.help.description"))));
    			commandSender.sendMessage(cmd2);
    			
    			TextComponent cmd3 = new TextComponent("    ");
    			TextComponent cmd3Perms = new TextComponent("§7§l· §a/bh reload");
    			cmd3Perms.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§fbungeehelp.reload")));
    			cmd3Perms.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/bh reload"));
    			cmd3.addExtra(cmd3Perms);
    			cmd3.addExtra("§8 - ".concat(Utils.replaceColors(Settings.config.getString("messages.commands.reload.description"))));
    			commandSender.sendMessage(cmd3);

    			if (plugin.getProxy().getConsole() != commandSender) {
					commandSender.sendMessage();
					commandSender.sendMessage(new TextComponent("    §8§oHover a command to see its permissions;"));
					commandSender.sendMessage(new TextComponent("    §8§oClick a command to run it;"));
				}

				commandSender.sendMessage();
    			commandSender.sendMessage(new TextComponent("§8§m+------------------+§e§l BungeeHelp §8§m+------------------+"));
    		} else {
	    		mainMessage.addExtra(Utils.replaceColors(Settings.config.getString("messages.no-permissions")));
	    		commandSender.sendMessage(mainMessage);
	    	}
	    } else if (args[0].equalsIgnoreCase("reload")) {
	    	if (commandSender.hasPermission("bungeehelp.reload")) {
		    	try {
					Settings.loadConfiguration("config.yml");

					mainMessage.addExtra(Utils.replaceColors(Settings.config.getString("messages.commands.reload.success")));
					commandSender.sendMessage(mainMessage);
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	} else {
	    		mainMessage.addExtra(Utils.replaceColors(Settings.config.getString("messages.no-permissions")));
	    		commandSender.sendMessage(mainMessage);
	    	}

	    } else {
	    	mainMessage.addExtra(Utils.replaceColors(Settings.config.getString("messages.unknown-command")));
	    	commandSender.sendMessage(mainMessage);
	    }
    }

    // Show subcommands on tab complete
	@Override
	public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
		List<String> matches;
		if (args.length == 0) {
			return Collections.emptyList();
		} else {
			ArrayList<String> subcommands = new ArrayList<>();
			subcommands.add("help");
			subcommands.add("reload");
			matches = subcommands.stream().filter(val -> val.startsWith(args[0])).collect(Collectors.toList());
		}
		return matches;
	}
}
