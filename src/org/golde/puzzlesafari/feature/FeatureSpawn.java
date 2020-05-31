package org.golde.puzzlesafari.feature;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.golde.puzzlesafari.utils.WarpManager;

public class FeatureSpawn extends FeatureBase {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		WarpManager.warpPlayer(e.getPlayer(), "spawn");
	}

}
