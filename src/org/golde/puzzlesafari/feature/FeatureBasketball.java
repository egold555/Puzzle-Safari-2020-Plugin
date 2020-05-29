package org.golde.puzzlesafari.feature;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.MaterialData;

public class FeatureBasketball extends FeatureBase {

	@EventHandler
	public void ballFiring(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		Action a = e.getAction();
		if (a != Action.RIGHT_CLICK_AIR || e.getItem() == null) {
			return;
		}

		if(e.getItem().getType() == Material.CONCRETE_POWDER) {
			fireSand(e.getPlayer());
		}


	}

	
	private void fireSand(Player player) {
		World world = player.getWorld();
		Location loc = player.getLocation().add(0, 2, 0);
		FallingBlock falling = world.spawnFallingBlock(loc, new MaterialData(Material.CONCRETE, (byte)5));
		falling.setFallDistance(1);
		
		//Thanks crackle <3
		falling.setVelocity(player.getLocation().getDirection().normalize().multiply(1.35));

	}

}
