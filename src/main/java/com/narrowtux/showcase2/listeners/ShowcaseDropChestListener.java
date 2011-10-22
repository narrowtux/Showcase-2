package com.narrowtux.showcase2.listeners;

import com.narrowtux.dropchest.api.DropChestListener;
import com.narrowtux.dropchest.api.DropChestSuckEvent;
import com.narrowtux.showcase2.types.Showcase;

public class ShowcaseDropChestListener extends DropChestListener {

	@Override
	public void onDropChestSuck(DropChestSuckEvent event) {
		if(event.isCancelled()) {
			return;
		}
		if(Showcase.getShowcase(event.getItem()) != null) {
			event.setCancelled(true);
		}
	}

}
