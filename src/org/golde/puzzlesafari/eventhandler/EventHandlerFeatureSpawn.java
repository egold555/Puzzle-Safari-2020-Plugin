package org.golde.puzzlesafari.eventhandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.golde.puzzlesafari.utils.WarpManager;

public class EventHandlerFeatureSpawn extends EventHandlerBase {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		WarpManager.warpPlayer(e.getPlayer(), "spawn");
	}

}
