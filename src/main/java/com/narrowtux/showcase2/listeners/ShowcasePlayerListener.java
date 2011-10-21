package com.narrowtux.showcase2.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.narrowtux.showcase2.ShowcaseMain;
import com.narrowtux.showcase2.ShowcasePlayer;
import com.narrowtux.showcase2.assistants.TypeSelectionAssistant;
import com.narrowtux.showcase2.event.ShowcaseCreationEvent;
import com.narrowtux.showcase2.event.ShowcaseInteractEvent;
import com.narrowtux.showcase2.event.ShowcaseRemoveEvent;
import com.narrowtux.showcase2.types.Showcase;
import com.narrowtux.showcase2.types.ShowcaseType;

public class ShowcasePlayerListener extends PlayerListener {
	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		Showcase sc;
		ShowcasePlayer player = ShowcasePlayer.getPlayer(event.getPlayer().getName());
		switch(event.getAction()) {
		case LEFT_CLICK_BLOCK:
			sc = ShowcaseMain.getInstance().getShowcase(event.getClickedBlock());
			if(sc == null 
					&& event.getClickedBlock().getType().equals(Material.STEP)
					&& player.isRequestingBuild()) {
				if(player.getRequestedType() != null) {
					ShowcaseType type = player.getRequestedType();
					sc = type.createShowcase(event.getClickedBlock(), event.getPlayer().getItemInHand(), player, player.getTypeArguments());
					if(!sc.onCreate(player)) {
						sc.remove();
					} else {
						ShowcaseCreationEvent sce = new ShowcaseCreationEvent(sc);
						Bukkit.getPluginManager().callEvent(sce);
						if(!sce.isCancelled()) {
							ShowcaseMain.getInstance().addShowcase(sc);
						} else {
							sc.remove();
						}
					}
					
				} else {
					TypeSelectionAssistant a = new TypeSelectionAssistant(event.getClickedBlock(), event.getPlayer().getItemInHand(), player);
					a.start();
				}
				player.resetRequest();
				event.setCancelled(true);
			} else if(sc != null) {
				ShowcaseInteractEvent sie = new ShowcaseInteractEvent(sc, event.getAction());
				Bukkit.getPluginManager().callEvent(sie);
				if(!sie.isCancelled()) {
					sc.onLeftClick(player);
				}
				event.setCancelled(true);
			}
			break;
		case RIGHT_CLICK_BLOCK:
			sc = ShowcaseMain.getInstance().getShowcase(event.getClickedBlock());
			if(sc != null && event.getPlayer().isSneaking() && sc.isOwner(player)) {
				if(sc.onRemove(player)) {
					ShowcaseRemoveEvent sre = new ShowcaseRemoveEvent(sc);
					Bukkit.getPluginManager().callEvent(sre);
					if(!sre.isCancelled()) {
						event.getPlayer().sendMessage("Removed showcase.");
						sc.remove();
					}
				}
				event.setCancelled(true);
			} else if(sc != null) {
				ShowcaseInteractEvent sie = new ShowcaseInteractEvent(sc, event.getAction());
				Bukkit.getPluginManager().callEvent(sie);
				if(!sie.isCancelled()) {
					sc.onRightClick(player);
				}
				event.setCancelled(true);
			}
			break;
		}
	}

	@Override
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		event.setCancelled(event.isCancelled() || Showcase.getShowcase(event.getItem()) != null);
	}
}
