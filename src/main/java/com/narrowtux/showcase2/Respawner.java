package com.narrowtux.showcase2;

import java.util.logging.Level;

import com.narrowtux.showcase2.types.Showcase;

public class Respawner implements Runnable {

	@Override
	public void run() {
		for(Showcase sc:ShowcaseMain.getInstance().getAllShowcases()) {
			if(sc.isChunkLoaded()) {
				if(sc.getItem().isDead()) {
					sc.respawn();
					ShowcaseMain.doLog("Respawned "+sc, Level.FINE);
				}
			}
		}
	}
}
