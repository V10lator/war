package com.tommytony.war;

import org.bukkit.*;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class War extends JavaPlugin {
	
	public War(PluginLoader pluginLoader, Server instance,
			PluginDescriptionFile desc, File plugin, ClassLoader cLoader) {
		super(pluginLoader, instance, desc, plugin, cLoader);
	}
	

	private WarPlayerListener playerListener = new WarPlayerListener(this);
	private WarEntityListener entityListener = new WarEntityListener(this);
	private WarBlockListener blockListener = new WarBlockListener(this);
    private Logger log;
    String name = "War";
    String version = "0.3";
    
    private final List<Warzone> warzones = new ArrayList<Warzone>();
    private final HashMap<Integer, ItemStack> defaultLoadout = new HashMap<Integer, ItemStack>();
    private int defaultLifepool = 7;
    private boolean defaultFriendlyFire = false;    
	
	
	public void onDisable() {
		Logger.getLogger("Minecraft").info(name + " " + version + " disabled.");
	}

	public void onEnable() {
		this.log = Logger.getLogger("Minecraft");
		
		// Register hMod hooks
		
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvent(Event.Type.PLAYER_LOGIN, playerListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);	// DISCONNECT
		pm.registerEvent(Event.Type.PLAYER_COMMAND, playerListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Normal, this);
		
		pm.registerEvent(Event.Type.ENTITY_DEATH, entityListener, Priority.Normal, this); //HEALTH_CHANGE
		pm.registerEvent(Event.Type.ENTITY_DAMAGEDBY_ENTITY, entityListener, Priority.Normal, this); //DAMAGE
		
		pm.registerEvent(Event.Type.BLOCK_IGNITE, blockListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_FLOW, blockListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_CANBUILD, blockListener, Priority.Normal, this);	// BLOCK_PLACE
		pm.registerEvent(Event.Type.BLOCK_DAMAGED, blockListener, Priority.Normal, this);	// BROKEN
		
		// Load files from disk or create them
		this.defaultLoadout.put(0, new ItemStack(272, 1));
		this.defaultLoadout.put(1, new ItemStack(261, 1));
		this.defaultLoadout.put(2, new ItemStack(262, 12));
		this.defaultLoadout.put(3, new ItemStack(274, 1));
		this.defaultLoadout.put(4, new ItemStack(273, 1));
		this.defaultLoadout.put(5, new ItemStack(275, 1));
		this.defaultLoadout.put(27, new ItemStack(259, 1));
		this.defaultLoadout.put(6, new ItemStack(297, 1));
		this.defaultLoadout.put(8, new ItemStack(3, 12));
		this.defaultLoadout.put(100, new ItemStack(301, 1));
		this.defaultLoadout.put(101, new ItemStack(300, 1));
		this.defaultLoadout.put(102, new ItemStack(299, 1));
		this.defaultLoadout.put(103, new ItemStack(298, 1));
		this.defaultLifepool = 7;
		this.defaultFriendlyFire = false;
		WarMapper.load(this);
		
		getLogger().info(name + " " + version + " enabled.");
	}

	public Team getPlayerTeam(String playerName) {
		for(Warzone warzone : warzones) {
			Team team = warzone.getPlayerTeam(playerName);
			if(team != null) return team;
		}
		return null;
	}
	
	public Warzone getPlayerWarzone(String playerName) {
		for(Warzone warzone : warzones) {
			Team team = warzone.getPlayerTeam(playerName);
			if(team != null) return warzone;
		}
		return null;
	}	

	public Logger getLogger() {
		return log;
	}
	
	public Warzone warzone(Location location) {
		for(Warzone warzone : warzones) {
			if(warzone.contains(location)) return warzone;
		}
		return null;
	}

	public boolean inAnyWarzone(Location location) {
		if(warzone(location) == null) {
			return false;
		}
		return true;
	}
	
	public boolean inWarzone(String warzoneName, Location location) {
		Warzone currentZone = warzone(location);
		if(currentZone == null) {
			return false;
		} else if (warzoneName.equals(currentZone.getName())){
			return true;
		}
		return false;
	}

	public void addWarzone(Warzone zone) {
		warzones.add(zone);
	}

	public List<Warzone> getWarzones() {
		return warzones;
	}
	
	public String str(String str) {
		String out = Color.GRAY + "[war] " + Color.WHITE + str;
		return out;
	}
	
	public Warzone findWarzone(String warzoneName) {
		for(Warzone warzone : warzones) {
			if(warzone.getName().equals(warzoneName)) {
				return warzone;
			}
		}
		return null;
	}

	public HashMap<Integer, ItemStack> getDefaultLoadout() {
		return defaultLoadout;
	}

	public void setDefaultLifepool(int defaultLifepool) {
		this.defaultLifepool = defaultLifepool;
	}

	public int getDefaultLifepool() {
		return defaultLifepool;
	}

	public void setDefaultFriendlyFire(boolean defaultFriendlyFire) {
		this.defaultFriendlyFire = defaultFriendlyFire;
	}

	public boolean getDefaultFriendlyFire() {
		return defaultFriendlyFire;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	
}