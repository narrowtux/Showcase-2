package com.narrowtux.showcase2.event;

import org.bukkit.event.Cancellable;

import com.narrowtux.showcase2.types.Showcase;

public class ShowcaseRemoveEvent extends ShowcaseEvent implements Cancellable {
	private static final long serialVersionUID = 2701905257095152554L;
	private boolean cancelled = false;

	public ShowcaseRemoveEvent(Showcase showcase) {
		super(showcase, "ShowcaseRemoveEvent");
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		cancelled = arg0;
	}

}
