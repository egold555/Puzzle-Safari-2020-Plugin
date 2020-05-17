package org.golde.puzzlesafari.feature;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.golde.puzzlesafari.Main;

public abstract class FeatureBase implements Listener {

	public abstract void onEnable();
	public void onDisable() {};
	
	protected final void registerEvents() {
		Bukkit.getPluginManager().registerEvents(this, getPlugin());
	}
	
	protected final Main getPlugin() {
		return Main.getInstance();
	}
	
	protected final String color(String msg) {
		return getPlugin().color(msg);
	}
	
	
	
}
