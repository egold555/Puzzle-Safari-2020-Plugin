package org.golde.puzzlesafari.feature;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FeatureMouseMaze extends FeatureBase {
	
	@Override
	public String getWarpTrigger() {
		return "mouse";
	}
	
	@Override
	public void onEnter(Player p) {
		
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SLIME_BLOCK && p.getLocation().subtract(0, 1, 0).getBlock().getRelative(BlockFace.DOWN).getType() == Material.STAINED_GLASS) {
			 p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20*2, 13));
		}
	}

}
