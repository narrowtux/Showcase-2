package com.narrowtux.showcase2.types;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.narrowtux.showcase2.ShowcaseMain;
import com.narrowtux.showcase2.ShowcasePlayer;

public class BasicShowcase extends Showcase {

	public BasicShowcase(Block block, ItemStack type, ShowcasePlayer owner) {
		super(block, type, owner);
	}

	@Override
	public boolean onCreate(ShowcasePlayer player) {
		if(ShowcaseMain.getCustomConfig().isBasicTakingItem()) {
			ItemStack remove = getType().clone();
			remove.setAmount(1);
			player.getPlayer().getInventory().removeItem(remove);
		}
		return true;
	}

	@Override
	public boolean onRemove(ShowcasePlayer player) {
		if(ShowcaseMain.getCustomConfig().isBasicTakingItem()) {
			ItemStack add = getType().clone();
			add.setAmount(1);
			if(player.getPlayer().getInventory().addItem(add).size()>0){
				player.sendMessage("You can't hold that item so the showcase wasn't removed");
				return false;
			}
		}
		return true;
	}
	
	

}
