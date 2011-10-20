package com.narrowtux.showcase2.event;

import org.bukkit.event.Cancellable;

import com.narrowtux.showcase2.types.Showcase;

public class ShowcaseCreationEvent extends ShowcaseEvent implements Cancellable {
	private static final long serialVersionUID = 606114683791360678L;
	private boolean cancelled = false;

	public ShowcaseCreationEvent(Showcase showcase) {
		super(showcase, "ShowcaseCreationEvent");
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
