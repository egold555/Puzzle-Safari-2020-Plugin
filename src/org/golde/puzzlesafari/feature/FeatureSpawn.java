package org.golde.puzzlesafari.feature;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.golde.puzzlesafari.utils.WarpManager;

public class FeatureSpawn extends FeatureBase {

	@Override
	public String getWarpTrigger() {
		return "spawn";
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		WarpManager.warpPlayer(e.getPlayer(), "spawn");
	}

}
