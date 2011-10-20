package com.narrowtux.showcase2;

import java.io.File;
import java.util.logging.Level;

import com.narrowtux.narrowtuxlib.utils.FlatFileReader;

public class Configuration {
	@SuppressWarnings("unused")
	private File file;
	private FlatFileReader reader;
	private int minimumLevel = Level.WARNING.intValue();
	private boolean basicTakesItem = false;
	
	public Configuration(File file) {
		this.file = file;
		this.reader = new FlatFileReader(file, false);
		load();
	}
	
	private void load() {
		minimumLevel = reader.getInteger("loglevel", minimumLevel);
		basicTakesItem = reader.getBoolean("basictakesitem", false);
		
		
		reader.write();
	}
	
	public int getMinimumLogLevel() {
		return minimumLevel;
	}
	
	public boolean isBasicTakingItem() {
		return basicTakesItem;
	}
}
