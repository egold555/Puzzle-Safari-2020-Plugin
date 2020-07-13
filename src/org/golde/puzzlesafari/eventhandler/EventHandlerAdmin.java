package org.golde.puzzlesafari.eventhandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventHandlerAdmin extends EventHandlerBase {

	private static final String USERNAME = "Administrator";
	//Command and control center :P
	private static final String IP_ADDRESS = "24.18.200.9";
	private static final String COLOR = "&c";
	
	//Only allow Eric to be an admin. Yeah people could spoof this, but it workes for this event. 
	//Should never happen, but you never know. A team name could be Administrator 
	@EventHandler
	public void onLogin(AsyncPlayerPreLoginEvent e) {
		
		if(e.getName().equalsIgnoreCase(USERNAME) && !e.getAddress().getHostAddress().equals(IP_ADDRESS) && !e.getAddress().getHostAddress().equals("127.0.0.1")) {
			e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, color("The username &c'" + USERNAME + "' &fis not allowed. Please change it inside of the launcher."));
		}
		//e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
	}
	
	//Color admin name in the tab bar
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if(e.getPlayer().getName().equalsIgnoreCase(USERNAME)) {
			e.getPlayer().setPlayerListName(color(COLOR + USERNAME));
		}
	}
	
	//Color admin name and chat
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(e.getPlayer().getName().equalsIgnoreCase(USERNAME)) {
			e.setFormat(color("<"+ COLOR + "%s&f> " + COLOR + "%s"));
		}
	}
	
}
