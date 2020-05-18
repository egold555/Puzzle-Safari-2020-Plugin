package org.golde.puzzlesafari.feature;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.golde.puzzlesafari.Main;

public abstract class FeatureBase implements Listener {

	
	public void onInternalEnable() {
		Bukkit.getPluginManager().registerEvents(this, getPlugin());
		onEnable();
	}
	public void onEnable() {}
	public void onDisable() {};
	
	protected final Main getPlugin() {
		return Main.getInstance();
	}
	
	protected final String color(String msg) {
		return getPlugin().color(msg);
	}
	
	protected final World getWorld() {
		return Bukkit.getWorld("world");
	}
	
	public String getWarpTrigger() {
		return null;
	}
	
	public void onEnter(Player p) {
		
	}
	
	protected final void setPlayerRespawnLocation(Player player, Location loc) {
		player.setBedSpawnLocation(loc, true);
		
	}
	
	
	
}
