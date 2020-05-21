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

	@Override
	public void onEnter(Player p) {
		
		
		
		//add regen
		p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*3, 10, true));
		
		//reset time
		p.resetPlayerTime();
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		WarpManager.warpPlayer(e.getPlayer(), "spawn");
	}

}
