package org.golde.puzzlesafari.eventhandler;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.golde.puzzlesafari.utils.WarpManager;

public class EventHandlerChatBegin extends EventHandlerBase {

	private static final String ERROR_MESSAGE = "&cI am sorry, but please check the event name and try again.";

	private static final Set<String> ALLOWED_WARPS = new HashSet<String>();
	static {
		ALLOWED_WARPS.add("zombie");
		ALLOWED_WARPS.add("parkour");
		ALLOWED_WARPS.add("spawn");
		ALLOWED_WARPS.add("mineshaft");
		ALLOWED_WARPS.add("skydiving");
		ALLOWED_WARPS.add("sheep");
		ALLOWED_WARPS.add("basketball");
		ALLOWED_WARPS.add("rat");
		ALLOWED_WARPS.add("elements");
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {

		String msg = e.getMessage().toLowerCase();
		Player p = e.getPlayer();

		if(msg.startsWith("begin ") || msg.startsWith("start ")) {
			String[] split = msg.split(" ");
			if(split.length == 2) {

				//This happends async, so I need to throw it into the Bukkit thread
				new BukkitRunnable() {

					@Override
					public void run() {
						String warp = split[1].toLowerCase();

						if(!ALLOWED_WARPS.contains(warp)) {
							p.sendMessage(color(ERROR_MESSAGE + " (Error code 1)"));
							return;
						}

						if(!WarpManager.warpExists(warp)) {
							p.sendMessage(color(ERROR_MESSAGE + " (Error code 2)"));
						}
						else {
							WarpManager.warpPlayer(p, warp);
						}
					}

				}.runTask(getPlugin());


			}

			else {
				p.sendMessage(color(ERROR_MESSAGE + " (Error code 3)"));
			}

			e.setCancelled(true);
		}
		else if(msg.contains("restart")) {
			new BukkitRunnable() {

				@Override
				public void run() {
					String lastWarp = WarpManager.getLastWarp(p);
					if(lastWarp != null) {
						WarpManager.warpPlayer(p, lastWarp);
					}
					else {
						p.sendMessage(ChatColor.RED + "I am not sure where to send you? Did you do 'begin <code>' before attempting to restart a challenge? If so this is a bug!" );
					}
				}
			}.runTask(getPlugin());
			e.setCancelled(true);
		}

	}

}
