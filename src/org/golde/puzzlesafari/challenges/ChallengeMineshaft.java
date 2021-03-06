package org.golde.puzzlesafari.challenges;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.golde.puzzlesafari.utils.cuboid.EndCuboid;
import org.golde.puzzlesafari.utils.cuboid.EndCuboid.EndCuboidCallback;

public class ChallengeMineshaft extends Challenge {

	@Override
	public String getWarpTrigger() {
		return "mineshaft";
	}
	
	@Override
	public String getTitle() {
		return "Mineshaft Madness";
	}
	
	@Override
	public void onEnable() {
		new EndCuboid(new Location(getWorld(), -171, 6, 90), new Location(getWorld(), -173, 8, 90), new EndCuboidCallback() {

			@Override
			public void onEnter(Player p) {
				sendFinishMessage(p, "red wall");
			}
		});
	}
	
	@Override
	public void onEnter(Player p) {
		sendEnterMessage(
				p, 
				"Its a long way down...", 
				"Avoid the rails",
				MOVEMENT_WASD,
				"&oHit the water, and you will survive"
				);
	}
	
}
