package com.narrowtux.showcase2.command;

import java.util.Collection;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.narrowtux.showcase2.ShowcasePlayer;
import com.narrowtux.showcase2.types.ShowcaseType;

public class TypesCommandHandler extends AbstractCommandHandler {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args, ShowcasePlayer player) {
		sender.sendMessage("Available types:");
		String types = "";
		Collection<ShowcaseType> atypes = ShowcaseType.getAllTypes();
		int i = 0;
		for(ShowcaseType type: atypes){
			types+=type.getName();
			if(i+1<atypes.size()){
				types+=", ";
			}
			i++;
		}
		sender.sendMessage(types);
		return true;
	}

	@Override
	public String getUsage() {
		return "";
	}

	@Override
	public String getSubCommandLabel() {
		return "types";
	}

	@Override
	public boolean isIngameOnly() {
		return false;
	}

	@Override
	public String getPermission() {
		return "showcase.listtypes";
	}

}
