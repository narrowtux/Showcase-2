package com.narrowtux.showcase2.types;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.narrowtux.showcase2.ShowcasePlayer;

public abstract class ShowcaseType {
	private static HashMap<String, ShowcaseType> types = new HashMap<String, ShowcaseType>();
	private String name;
	
	public ShowcaseType(String name) {
		this.name = name;
	}
	
	public static ShowcaseType getType(String name) {
		return types.get(name);
	}
	
	public final String getName() {
		return name;
	}
	
	public static Collection<ShowcaseType> getAllTypes() {
		return types.values();
	}
	
	/**
	 * This is the factory method of your showcase type. Return a new instance of a Showcase object.
	 * @param block the block the showcase is in.
	 * @param stack the items of the showcase
	 * @param player the player who builds the showcase
	 * @param args arguments from the console. This is null when the player is using the selection assistant instead or if he doesn't pass additional arguments.
	 * @return a showcase object
	 */
	public abstract Showcase createShowcase(Block block, ItemStack stack, ShowcasePlayer player, String args[]);
	
	public static void registerType(ShowcaseType type) {
		types.put(type.getName(), type);
	}
}
