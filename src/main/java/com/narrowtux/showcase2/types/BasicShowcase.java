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
	public boolean onCreate() {
		if(ShowcaseMain.getConfig().isBasicTakingItem()) {
			ItemStack remove = getType().clone();
			remove.setAmount(1);
			getOwner().getPlayer().getInventory().removeItem(remove);
		}
		return true;
	}

	@Override
	public boolean onRemove() {
		if(ShowcaseMain.getConfig().isBasicTakingItem()) {
			ItemStack add = getType().clone();
			add.setAmount(1);
			if(getOwner().getPlayer().getInventory().addItem(add).size()>0){
				getOwner().sendMessage("You can't hold that item so the showcase wasn't removed");
				return false;
			}
		}
		return true;
	}
	
	

}
