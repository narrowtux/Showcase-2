package com.narrowtux.showcase2.types;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.narrowtux.showcase2.ShowcasePlayer;

public class BasicType extends ShowcaseType {
	public BasicType() {
		super("basic");
	}

	@Override
	public Showcase createShowcase(Block block, ItemStack stack, ShowcasePlayer player, String args[]) {
		return new BasicShowcase(block, stack, player);
	}
}
