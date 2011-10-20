package com.narrowtux.showcase2.event;

import org.bukkit.event.Event;

import com.narrowtux.showcase2.types.Showcase;

public class ShowcaseEvent extends Event {
	private static final long serialVersionUID = 7024450850892041516L;
	private Showcase showcase;
	
	protected ShowcaseEvent(Showcase showcase, String name) {
		super(name);
		this.showcase = showcase;
	}
	
	public Showcase getShowcase() {
		return showcase;
	}
}
