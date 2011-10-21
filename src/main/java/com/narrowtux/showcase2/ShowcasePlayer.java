package com.narrowtux.showcase2;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.narrowtux.showcase2.types.Showcase;
import com.narrowtux.showcase2.types.ShowcaseType;

public class ShowcasePlayer {
	private String name;
	private static HashMap<String, ShowcasePlayer> instances = new HashMap<String, ShowcasePlayer>();
	private HashSet<Showcase> showcases = new HashSet<Showcase>();
	private boolean requestedBuild = false;
	private ShowcaseType requestedType = null;
	private String typeArguments[];
	private boolean breaking = true;
	
	private ShowcasePlayer(String name) {
		this.name = name;
		instances.put(name, this);
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayerExact(name);
	}
	
	public static ShowcasePlayer getPlayer(String name) {
		if(!instances.containsKey(name)) {
			new ShowcasePlayer(name);
		}
		return instances.get(name);
	}
	
	public void addShowcase(Showcase sc) {
		showcases.add(sc);
	}
	
	public void removeShowcase(Showcase sc) {
		showcases.remove(sc);
	}
	
	public Set<Showcase> getAllShowcases() {
		return Collections.unmodifiableSet(showcases);
	}

	public String getName() {
		return name;
	}
	
	public void requestBuild() {
		requestedBuild = true;
	}
	
	public boolean isRequestingBuild() {
		return requestedBuild;
	}
	
	public void setRequestedType(ShowcaseType type, String ...args) {
		requestedType = type;
		typeArguments = args;
	}
	
	public void resetRequest() {
		requestedBuild = false;
		requestedType = null;
		typeArguments = null;
	}
	
	public ShowcaseType getRequestedType() {
		return requestedType;
	}
	
	public String[] getTypeArguments() {
		return typeArguments;
	}

	public void sendMessage(String string) {
		getPlayer().sendMessage(string);
	}

	@Override
	public boolean equals(Object arg0) {
		if(arg0 instanceof ShowcasePlayer) {
			return name.equals(((ShowcasePlayer)arg0).name);
		}
		return false;
	}

	public void setCanBreak(boolean b) {
		breaking = b;
	}
	
	public boolean canBreak() {
		return breaking;
	}
}
