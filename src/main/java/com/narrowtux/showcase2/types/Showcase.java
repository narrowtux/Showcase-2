package com.narrowtux.showcase2.types;

import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.narrowtux.showcase2.ShowcaseMain;
import com.narrowtux.showcase2.ShowcasePlayer;

public class Showcase {
	private Block block = null;
	private ShowcasePlayer owner;
	private ItemStack type = new ItemStack(0);
	protected Item item;
	private ShowcaseType showcaseType = null;
	private static HashMap<Item, Showcase> showcasesForItems = new HashMap<Item, Showcase>();
	private Chunk chunk;
	
	public Showcase(Block block, ItemStack type, ShowcasePlayer owner) {
		this.type = type.clone();
		this.type.setAmount(1);
		
		this.block = block;
		
		this.chunk = block.getChunk();
		
		this.owner = owner;
		this.owner.addShowcase(this);
		
		spawn();
	}

	/**
	 * Spawns the item on the block.
	 */
	public void spawn() {
		if (block == null) {
			ShowcaseMain.doLog("Can't spawn Showcase with type "+type.toString()+" because block is null", Level.WARNING);
			return;
		}
		Location spawn = block.getLocation();
		spawn.add(0.5, 0.6, 0.5);
		item = block.getWorld().dropItem(spawn, getType().clone());
		
		showcasesForItems.put(item, this);
		
		//Push the item up once so it doesn't fall through the block (work around ugly minecraft physics)
		item.setVelocity(new Vector(0, 0.1, 0));
	}
	
	/**
	 * Removes the item from the block.
	 */
	public void removeItem() {
		if(item != null) {
			showcasesForItems.remove(item);
			item.remove();
		}
		item = null;
	}
	
	/**
	 * Deletes the showcase from the system.
	 */
	public void remove() {
		removeItem();
		ShowcaseMain.getInstance().removeShowcase(this);
	}
	
	/**
	 * Respawns the item.
	 */
	public void respawn() {
		removeItem();
		spawn();
	}
	
	public ShowcasePlayer getOwner() {
		return owner;
	}
	
	/**
	 * Sets the new owner of the showcase
	 * @param owner
	 */
	public void setOwner(ShowcasePlayer owner) {
		if(this.owner != null) {
			this.owner.removeShowcase(this);
		}
		this.owner = owner;
		this.owner.addShowcase(this);
	}
	
	/**
	 * Called when someone clicks on the showcase with left mouse button
	 * @param player the player who clicked on this showcase
	 */
	public void onLeftClick(ShowcasePlayer player) {
	}
	
	/**
	 * Called when someone clicks on the showcase with right mouse button
	 * @param player the player who clicked on this showcase
	 */
	public void onRightClick(ShowcasePlayer player) {
		
	}
	
	/**
	 * Called when the showcase was created-
	 * @return false if the item shouldn't be created
	 */
	public boolean onCreate() {
		
		return true;
	}
	
	/**
	 * Called when the showcase should be removed
	 * @return false if the item shouldn't be removed
	 */
	public boolean onRemove() {
		
		return true;
	}
	
	/**
	 * Called when saving is done. Put key=value pairs into the map to save your stuff
	 * @param map the map to save in. Do not overwrite the instance ;)
	 */
	public void doSave(HashMap<String, Object> map) {
		
	}
	
	/**
	 * Called when loading is done. Get values from your saved keys from the map to load your stuff
	 * @param map the map to load from.
	 */
	public void doLoad(HashMap<String, Object> map) {
		
	}

	/**
	 * Gets the block of the showcase.
	 * @return the block
	 */
	public Block getBlock() {
		return block;
	}
	
	/**
	 * Gets the type of the showcase.
	 * @return the type
	 */
	public ItemStack getType() {
		return this.type.clone();
	}
	
	public final void setShowcaseType(ShowcaseType type) {
		if(this.showcaseType == null) {
			this.showcaseType = type;
		} else {
			throw new IllegalStateException("This showcase already has a type");
		}
	}
	
	public final ShowcaseType getShowcaseType() {
		return showcaseType;
	}
	
	public static Showcase getShowcase(Item item) {
		return showcasesForItems.get(item);
	}

	public Item getItem() {
		return item;
	}
	
	public Chunk getChunk() {
		return chunk;
	}

	public boolean isChunkLoaded() {
		return chunk.isLoaded();
	}
}
