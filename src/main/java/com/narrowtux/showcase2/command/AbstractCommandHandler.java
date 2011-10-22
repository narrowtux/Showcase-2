package com.narrowtux.showcase2.command;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.narrowtux.showcase2.ShowcasePlayer;

public abstract class AbstractCommandHandler {
	private static HashMap<String, AbstractCommandHandler> handlers = new HashMap<String, AbstractCommandHandler>();
	
	public static boolean handleCommand(CommandSender sender, Command cmd, String label, String args[]) {
		ShowcasePlayer player = null;
		if(sender instanceof Player) {
			player = ShowcasePlayer.getPlayer(((Player)sender).getName());
		}
		if(args.length >= 1) {
			String sub = args[0];
			AbstractCommandHandler handler = handlers.get(sub);
			if(handler != null) {
				String subargs[] = new String[args.length-1];
				for(int i = 1; i<args.length;i++) {
					subargs[i-1] = args[i];
				}
				if(player == null && handler.isIngameOnly()) {
					sender.sendMessage("This can be used ingame only");
					return true;
				}
				return handler.onCommand(sender, cmd, sub, subargs, player);
			} else {
				sender.sendMessage("Subcommand '"+sub+"' not found.");
				printHelp(sender);
				return true;
			}
		}
		printHelp(sender);
		return true;
	}
	
	public static void registerHandler(AbstractCommandHandler handler) {
		handlers.put(handler.getSubCommandLabel(), handler);
	}
	
	public static void printHelp(CommandSender sender) {
		sender.sendMessage("Showcase command reference");
		for(AbstractCommandHandler handler:handlers.values()) {
			String cmd = "/sc";
			if(!(sender instanceof Player)) {
				if(handler.isIngameOnly()){
					continue;
				}
				cmd = "sc";
			}
			sender.sendMessage(ChatColor.YELLOW + cmd + " " + handler.getSubCommandLabel() + ChatColor.WHITE + " " + handler.getUsage());
		}
	}

	public abstract boolean onCommand(CommandSender sender, Command cmd, String label, String args[], ShowcasePlayer player);
	public abstract String getUsage();
	public abstract String getSubCommandLabel();
	public abstract boolean isIngameOnly();
	public abstract String getPermission();
}
