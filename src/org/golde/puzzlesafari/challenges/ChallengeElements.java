package org.golde.puzzlesafari.challenges;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.golde.puzzlesafari.utils.WarpManager;

public class ChallengeElements extends Challenge {

	private static final String[] WORDS = {"mercury", "bismuth", "sodium"};
	
	@Override
	public String getWarpTrigger() {
		return "elements";
	}
	
	@Override
	public String getTitle() {
		return "Perspective Elements";
	}
	
	@Override
	public void onEnter(Player p) {
		sendEnterMessage(
				p, 
				"After you finish, Press &aC&f, then type all the words", 
				"Find 3 hidden elements", 
				MOVEMENT_WASD, 
				"&7&oHey want to hear a joke about potassium?... K"
				);
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 8, true ,false));
		p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1, true ,false));
		
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		
		String msg = e.getMessage().toLowerCase();
		
		for(String word : WORDS) {
			if(msg.contains(word)) {
				e.setCancelled(true);
			}
		}
		
		if(msg.contains(WORDS[0]) && msg.contains(WORDS[1]) && msg.contains(WORDS[2])) {
			new BukkitRunnable() {

				@Override
				public void run() {
					Player p = e.getPlayer();
					reset(p);
					WarpManager.warpPlayer(p, "elementsend");
					sendFinishMessage(p, "periodic table");
				}
				
			}.runTaskLater(getPlugin(), 2);
			
		}
		
	}
	
}
