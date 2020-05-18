package org.golde.puzzlesafari.feature;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FeatureSpawn extends FeatureBase {

	@Override
	public void onEnable() {

	}

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

}