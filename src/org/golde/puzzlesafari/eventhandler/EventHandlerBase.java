package org.golde.puzzlesafari.eventhandler;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.golde.puzzlesafari.Main;

public abstract class EventHandlerBase implements Listener {


	public final void onInternalEnable() {
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

	

}
