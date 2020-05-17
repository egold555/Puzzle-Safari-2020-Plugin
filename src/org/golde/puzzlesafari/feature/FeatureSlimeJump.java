package org.golde.puzzlesafari.feature;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FeatureSlimeJump extends FeatureBase {

	@Override
	public void onEnable() {
		registerEvents();
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SLIME_BLOCK) {
			 p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20*2, 13));
		}
	}

}
