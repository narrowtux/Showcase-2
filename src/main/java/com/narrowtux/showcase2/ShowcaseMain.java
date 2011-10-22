package com.narrowtux.showcase2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.yaml.snakeyaml.Yaml;

import com.narrowtux.showcase2.listeners.ShowcaseBlockListener;
import com.narrowtux.showcase2.listeners.ShowcasePlayerListener;
import com.narrowtux.showcase2.listeners.ShowcaseWorldListener;
import com.narrowtux.showcase2.types.BasicType;
import com.narrowtux.showcase2.types.Showcase;
import com.narrowtux.showcase2.types.ShowcaseType;

public class ShowcaseMain extends JavaPlugin {
	private static ShowcaseMain instance;
	private HashMap<Location, Showcase> showcases = new HashMap<Location, Showcase>();
	private ShowcaseBlockListener blockListener = new ShowcaseBlockListener();
	private ShowcasePlayerListener playerListener = new ShowcasePlayerListener();
	private ShowcaseWorldListener worldListener = new ShowcaseWorldListener();
	private Configuration config;
	
	@Override
	public void onEnable() {
		instance = this;
		
		config = new Configuration(new File(getDataFolder(), "showcase.cfg"));
		
		ShowcaseMain.doLog("Enable started", Level.FINE);
		
		//Register event
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.Normal, instance);
		pm.registerEvent(Type.BLOCK_BREAK, blockListener, Priority.Normal, instance);
		pm.registerEvent(Type.BLOCK_PLACE, blockListener, Priority.Normal, instance);
		pm.registerEvent(Type.PLAYER_PICKUP_ITEM, playerListener, Priority.Normal, instance);
		pm.registerEvent(Type.CHUNK_LOAD, worldListener, Priority.Normal, instance);
		pm.registerEvent(Type.CHUNK_UNLOAD, worldListener, Priority.Normal, instance);
		
		//Register timers
		BukkitScheduler scheduler = getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(this, new Respawner(), 20, 20);
		
		//Register default types
		new BasicType();
		
		//Load stuff
		load();
		
		printStatus(true);
	}
	
	@Override
	public void onDisable() {
		try {
			save();
		} catch (Exception e) {
			doLog("Saving failed, give this log to narrowtux. Some Showcases could be lost!", Level.SEVERE);
			e.printStackTrace();
			doLog("End of log.", Level.SEVERE);
		}
		for(Showcase sc:showcases.values()) {
			sc.removeItem();
		}
		printStatus(false);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		ShowcasePlayer player = null;
		if(sender instanceof Player) {
			player = ShowcasePlayer.getPlayer(((Player)sender).getName());
		}
		boolean ingame = false;
		if(command.getName().equals("showcase")) {
			if(args.length >= 1 && args[0].equals("add")) {
				if(player != null) {
					player.requestBuild();
					if(args.length >= 2) {
						String type = args[1];
						ShowcaseType shty = ShowcaseType.getType(type);
						if(shty == null) {
							player.sendMessage("The type '"+shty+"' does not exist. Type /sc types to get a list.");
							return true;
						}
						String typeargs[] = null;
						if(args.length >= 3) {
							typeargs = new String[args.length-2];
							for(int i = 2; i<args.length; i++){
								typeargs[i-2] = args[i];
							}
						}
						
						player.setRequestedType(shty, typeargs);
					}
					player.sendMessage("Click on a slab to create a showcase with the item you're holding.");
					return true;
				} else {
					ingame = true;
				}
			}
			if(args.length >= 1 && args[0].equals("types")) {
				sender.sendMessage("TODO");
				return true;
			}
		}
		if(ingame) {
			sender.sendMessage("You can use this command ingame only.");
		}
		return false;
	}

	public static ShowcaseMain getInstance() {
		return instance;
	}

	public Showcase getShowcase(Block block) {
		return showcases.get(block.getLocation());
	}
	
	public void addShowcase(Showcase sc) {
		showcases.put(sc.getBlock().getLocation(), sc);
	}

	public void removeShowcase(Showcase sc) {
		for(ShowcasePlayer player:sc.getOwners()) {
			player.removeShowcase(sc);
		}
		showcases.remove(sc.getBlock().getLocation());
	}
	
	public Collection<Showcase> getAllShowcases() {
		return showcases.values();
	}

	@SuppressWarnings("unchecked")
	private void load() {
		Yaml yaml = new Yaml();
		doLog("Loading started...", Level.FINE);
		try {
			ArrayList<Object> dump = (ArrayList<Object>) yaml.load(new FileReader(getSaveFile()));
			for(Object o:dump) {
				HashMap<String, Object> item = (HashMap<String, Object>)o;
				int x = (Integer)item.get("x");
				int y = (Integer)item.get("y");
				int z = (Integer)item.get("z");
				String worldName = (String)item.get("world");
				int environment = (Integer)item.get("environment");
				int material = (Integer)item.get("material");
				int data = (Integer)item.get("data");
				String scType = (String)item.get("type");
				
				ArrayList<String> owners = (ArrayList<String>) item.get("owners");
				
				
				World w = new WorldCreator(worldName).environment(Environment.getEnvironment(environment)).createWorld();
				if(w == null) {
					doLog("Couldn't find world "+worldName, Level.SEVERE);
					continue;
				}
				Block b = w.getBlockAt(x,y,z);
				if(b == null) {
					doLog("Couldn't find block", Level.SEVERE);
					continue;
				}
				ItemStack stack = new ItemStack(material, 1, (short) data);
				ShowcaseType type = ShowcaseType.getType(scType);
				if(type == null) {
					doLog("Couldn't find type: "+scType, Level.SEVERE);
					continue;
				}
				ShowcasePlayer player = ShowcasePlayer.getPlayer(owners.get(0));
				Showcase sc = new Showcase(b, stack, player);
				for(String name:owners) {
					sc.addOwner(ShowcasePlayer.getPlayer(name));
				}
				sc.setShowcaseType(type);
				try{
					HashMap<String, Object> extra = (HashMap<String, Object>) item.get("extra");
					if(extra != null) {
						sc.doLoad(extra);
					}
				} catch(Exception e) {
					doLog("Error on loading type "+scType, Level.SEVERE);
					e.printStackTrace();
					sc.remove();
					continue;
				}
				addShowcase(sc);	
			}
		} catch (FileNotFoundException e) {
			doLog("Problem with loading: "+e.getMessage(), Level.SEVERE);
			return;
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			doLog("Invalid file.", Level.SEVERE);
			e.printStackTrace();
		}
		doLog("Loading done.", Level.FINER);
	}

	public void save() {
		Yaml yaml = new Yaml();
		ArrayList<Object> dump = new ArrayList<Object>();
		doLog("Saving...", Level.FINE);
		
		for(Showcase sc:showcases.values()) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("x", sc.getBlock().getX());
			item.put("y", sc.getBlock().getY());
			item.put("z", sc.getBlock().getZ());
			item.put("world", sc.getBlock().getWorld().getName());
			item.put("environment", sc.getBlock().getWorld().getEnvironment().getId());
			
			ArrayList<String> owners = new ArrayList<String>();
			for(ShowcasePlayer owner:sc.getOwners()) {
				owners.add(owner.getName());
			}
			item.put("owners", owners);
			
			item.put("material", sc.getType().getTypeId());
			item.put("data", sc.getType().getDurability());
			item.put("type", sc.getShowcaseType().getName());
			
			HashMap<String, Object> type = new HashMap<String, Object>();
			sc.doSave(type);
			item.put("extra", type);
			
			dump.add(item);
		}
		
		try {
			yaml.dump(dump, new FileWriter(getSaveFile()));
		} catch (IOException e) {
			doLog("Problem with saving: "+e.getMessage(), Level.SEVERE);
		}
		doLog("Saved.", Level.FINER);
	}
	
	protected File getSaveFile() {
		if(!getDataFolder().exists()) {
			if(!getDataFolder().mkdirs()) {
				doLog("Couldn't create Showcase directory, saving will not work. Try creating it yourself.", Level.SEVERE);
			}
		}
		return new File(getDataFolder(), "showcases.yml");
	}
	
	/**
	 * Prints on the console using ConsoleCommandSender (which can use ChatColor)
	 * The format will be [Plugin] msg
	 * @param msg
	 */
	public static void doLog(String msg, Level level) {
		if(level.intValue() < getCustomConfig().getMinimumLogLevel()){
			return;
		}
		String print = "[";
		ChatColor color = ChatColor.BLACK;
		if(level.equals(Level.SEVERE)) {
			color = ChatColor.RED;
		}
		if(level.equals(Level.WARNING)) {
			color = ChatColor.GOLD;
		}
		if(level.equals(Level.INFO)) {
			color = ChatColor.BLACK;
		}
		if(level.equals(Level.FINE)) {
			color = ChatColor.DARK_GREEN;
		}
		if(level.equals(Level.FINER)){
			color = ChatColor.GREEN;
		}
		if(level.equals(Level.FINEST)){
			color = ChatColor.GREEN;
		}
		print += color.toString();
		print += instance.getDescription().getName()+ChatColor.BLACK+"] ";
		print += msg;
		instance.getServer().getConsoleSender().sendMessage(print);
	}

	protected void printStatus(boolean enable) {
		String print = "";
		PluginDescriptionFile pdf = getDescription();
		print+= "["+ChatColor.GREEN+pdf.getName()+ChatColor.BLACK+"] by [";
		int i = 0;
		for(String author:pdf.getAuthors()) {
			print+=ChatColor.GREEN+author+ChatColor.BLACK;
			if(i+1<pdf.getAuthors().size()) {
				print +=", ";
			} else {
				print +="] ";
			}
			i++;
		}
		print+="version ["+ChatColor.GREEN+pdf.getVersion()+ChatColor.BLACK+"] ";
		if(enable) {
			print+=ChatColor.GREEN+"enabled";
		} else {
			print+=ChatColor.RED+"disabled";
		}
		print+=ChatColor.BLACK+".";
		
		getServer().getConsoleSender().sendMessage(print);
	}
	
	public static Configuration getCustomConfig(){
		return instance.config;
	}
}
