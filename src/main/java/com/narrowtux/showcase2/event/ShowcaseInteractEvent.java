package com.narrowtux.showcase2.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.block.Action;

import com.narrowtux.showcase2.types.Showcase;

public class ShowcaseInteractEvent extends ShowcaseEvent implements Cancellable {
	private static final long serialVersionUID = -6318054443854554457L;
	private boolean cancelled = false;
	private Action action;

	public ShowcaseInteractEvent(Showcase showcase, Action action) {
		super(showcase, "ShowcaseInteractEvent");
		this.action = action;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		cancelled = arg0;
	}
	
	public Action getAction() {
		return action;
	}

}
