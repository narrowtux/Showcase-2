package com.narrowtux.showcase2.listeners;

import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.narrowtux.showcase2.ShowcaseMain;
import com.narrowtux.showcase2.ShowcasePlayer;

public class ShowcaseBlockListener extends BlockListener {
	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		ShowcasePlayer player = ShowcasePlayer.getPlayer(event.getPlayer().getName());
		if(!player.canBreak()){
			event.setCancelled(true);
		} else {
			event.setCancelled(event.isCancelled() || ShowcaseMain.getInstance().getShowcase(event.getBlock()) != null);
		}
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		if(event.getBlockReplacedState().getType().equals(Material.STEP)) {
			if(ShowcaseMain.getInstance().getShowcase(event.getBlockAgainst()) != null) {
				event.setCancelled(true);
			}
		}
		if(event.getBlockReplacedState().getType().equals(Material.DOUBLE_STEP)) {
			if(ShowcaseMain.getInstance().getShowcase(event.getBlockReplacedState().getBlock()) != null) {
				event.setCancelled(true);
			}
		}
	}
}
