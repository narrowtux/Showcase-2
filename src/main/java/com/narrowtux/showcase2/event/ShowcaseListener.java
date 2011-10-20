package com.narrowtux.showcase2.event;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;

public class ShowcaseListener extends CustomEventListener {
	public void onShowcaseCreation(ShowcaseCreationEvent event) {
		
	}
	
	public void onShowcaseRemove(ShowcaseRemoveEvent event) {
		
	}

	public void onShowcaseInteract(ShowcaseInteractEvent event) {
		
	}
	
	@Override
	public void onCustomEvent(Event event) {
		if(event instanceof ShowcaseCreationEvent) {
			onShowcaseCreation((ShowcaseCreationEvent)event);
		}
		if(event instanceof ShowcaseRemoveEvent) {
			onShowcaseRemove((ShowcaseRemoveEvent)event);
		}
		if(event instanceof ShowcaseInteractEvent) {
			onShowcaseInteract((ShowcaseInteractEvent)event);
		}
	}
}
