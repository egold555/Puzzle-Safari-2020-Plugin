package org.golde.puzzlesafari.feature;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.golde.puzzlesafari.Main;
import org.golde.puzzlesafari.utils.ChatUtil;

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
	
	protected void sendEnterMessage(Player p, String title, String desc, String goal) {
		sendEnterMessage(p, title, desc, goal, null);
	}
	
	protected void sendEnterMessage(Player p, String title, String desc, String goal, String extra) {
		p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.8f, 1.0f);
		ChatUtil.sendCentredMessage(p, "&c&m" + StringUtils.repeat(" ", 80));
		
		ChatUtil.sendCentredMessage(p, "&e&l" + title);
		p.sendMessage("");
		ChatUtil.sendCentredMessage(p, desc);
		ChatUtil.sendCentredMessage(p, "&6Your goal: " + goal);
		p.sendMessage("");
		ChatUtil.sendCentredMessage(p, "Use &bWASD&f to move, &bSpace&f to jump.");
		if(extra != null) {
			ChatUtil.sendCentredMessage(p, extra);
		}
		
		
		ChatUtil.sendCentredMessage(p, "&c&m" + StringUtils.repeat(" ", 80));
	}
	
	protected void sendFinishMessage(Player p, String title, String pictureOf) {
		p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.8f, 0.1f);
		ChatUtil.sendCentredMessage(p, "&c&m" + StringUtils.repeat(" ", 80));
		
		ChatUtil.sendCentredMessage(p, "&e&l" + title);
		p.sendMessage("");
		ChatUtil.sendCentredMessage(p, "Congrats! You finished the task!");
		p.sendMessage("");
		ChatUtil.sendCentredMessage(p, "&6Take a picture of &a" + pictureOf + " &6and submit it!");
		
		ChatUtil.sendCentredMessage(p, "&c&m" + StringUtils.repeat(" ", 80));
	}
	
}
