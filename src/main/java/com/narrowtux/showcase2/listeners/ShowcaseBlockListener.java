package com.narrowtux.showcase2.listeners;

import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.narrowtux.showcase2.ShowcaseMain;

public class ShowcaseBlockListener extends BlockListener {
	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		event.setCancelled(event.isCancelled() || ShowcaseMain.getInstance().getShowcase(event.getBlock()) != null);
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		if(event.getBlockReplacedState().getType().equals(Material.STEP)) {
			if(ShowcaseMain.getInstance().getShowcase(event.getBlockAgainst()) != null) {
				event.setCancelled(true);
			}
		}
	}
}
