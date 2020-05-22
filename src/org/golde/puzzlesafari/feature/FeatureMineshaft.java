package org.golde.puzzlesafari.feature;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.golde.puzzlesafari.utils.cuboid.EndCuboid;
import org.golde.puzzlesafari.utils.cuboid.EndCuboid.EndCuboidCallback;

public class FeatureMineshaft extends FeatureBase {

	@Override
	public String getWarpTrigger() {
		return "mineshaft";
	}
	
	@Override
	public void onEnable() {
		new EndCuboid(new Location(getWorld(), -171, 6, 90), new Location(getWorld(), -173, 8, 90), new EndCuboidCallback() {

			@Override
			public void onEnter(Player p) {
				sendFinishMessage(p, "Mineshaft Madness", "the red wall");
			}
		});
	}
	
	@Override
	public void onEnter(Player p) {
		sendEnterMessage(
				p, 
				"Mineshaft Madness",
				"Its a long way down...", 
				"Avoid the rails",
				MOVEMENT_WASD,
				"&oHit the water, and you will survive"
				);
	}
	
}
