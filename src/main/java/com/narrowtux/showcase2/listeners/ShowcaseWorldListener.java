package com.narrowtux.showcase2.listeners;

import java.util.logging.Level;

import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;

import com.narrowtux.showcase2.ShowcaseMain;
import com.narrowtux.showcase2.types.Showcase;

public class ShowcaseWorldListener extends WorldListener {
	@Override
	public void onChunkLoad(ChunkLoadEvent event) {
		for(Showcase sc:ShowcaseMain.getInstance().getAllShowcases()) {
			if(sc.getChunk().equals(event.getChunk())) {
				sc.spawn();
				ShowcaseMain.doLog("Spawned "+sc, Level.FINE);
			}
		}
	}

	@Override
	public void onChunkUnload(ChunkUnloadEvent event) {
		for(Showcase sc:ShowcaseMain.getInstance().getAllShowcases()) {
			if(sc.getChunk().equals(event.getChunk())) {
				sc.removeItem();
				ShowcaseMain.doLog("Removed "+sc, Level.FINE);
			}
		}
	}
}
