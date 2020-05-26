package org.golde.puzzlesafari.feature;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.golde.puzzlesafari.utils.WarpManager;

public class FeatureChatBegin extends FeatureBase {

	private static final String ERROR_MESSAGE = "&cI am sorry, but please check the event name and try again.";
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		
		String msg = e.getMessage();
		Player p = e.getPlayer();
		
		if(msg.toLowerCase().startsWith("begin ")) {
			String[] split = msg.split(" ");
			if(split.length == 2) {
				
				//This happends async, so I need to throw it into the Bukkit thread
				new BukkitRunnable() {

					@Override
					public void run() {
						String warp = split[1].toLowerCase();
						
						if(warp.endsWith("end") || warp.endsWith("final") || warp.endsWith("2")) {
							p.sendMessage(color(ERROR_MESSAGE));
							return;
						}
						
						if(!WarpManager.warpExists(warp)) {
							p.sendMessage(color(ERROR_MESSAGE));
						}
						else {
							WarpManager.warpPlayer(p, warp);
						}
					}
					
				}.runTask(getPlugin());
				
				
			}
			else {
				p.sendMessage(color(ERROR_MESSAGE));
			}
			
			e.setCancelled(true);
		}
		
	}
	
}
