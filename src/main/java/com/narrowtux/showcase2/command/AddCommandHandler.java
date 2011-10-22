package com.narrowtux.showcase2.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.narrowtux.showcase2.ShowcasePlayer;
import com.narrowtux.showcase2.types.ShowcaseType;

public class AddCommandHandler extends AbstractCommandHandler {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args, ShowcasePlayer player) {
		if(player != null) {
			player.requestBuild();
			if(args.length >= 1) {
				String type = args[0];
				ShowcaseType shty = ShowcaseType.getType(type);
				if(shty == null) {
					player.sendMessage("The type '"+shty+"' does not exist. Type /sc types to get a list.");
					return true;
				}
				String typeargs[] = null;
				if(args.length >= 2) {
					typeargs = new String[args.length-1];
					for(int i = 2; i<args.length; i++){
						typeargs[i-1] = args[i];
					}
				}
				
				player.setRequestedType(shty, typeargs);
			}
			player.sendMessage("Click on a slab to create a showcase with the item you're holding.");
			return true;
		}
		return true;
	}

	@Override
	public String getUsage() {
		return "[ type [ args ... ] ]";
	}

	@Override
	public String getSubCommandLabel() {
		return "add";
	}

	@Override
	public boolean isIngameOnly() {
		return true;
	}

	@Override
	public String getPermission() {
		return "showcase.add";
	}

}
