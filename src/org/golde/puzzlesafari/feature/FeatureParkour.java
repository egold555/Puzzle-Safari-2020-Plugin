package org.golde.puzzlesafari.feature;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.golde.puzzlesafari.utils.Cuboid;
import org.golde.puzzlesafari.utils.MobCuboid;

public class FeatureParkour extends FeatureBase {

	private Cuboid health;
	private Cuboid checkpoint;
	private MobCuboid guardians;
	
	@Override
	public void onEnable() {
		
		
		health = new Cuboid(new Location(getWorld(), 134, 10, 56), new Location(getWorld(), 134, 11, 62));
		checkpoint = new Cuboid(new Location(getWorld(), 113, 4, 32), new Location(getWorld(), 118, 24, 86));
		guardians = new MobCuboid(new Location(getWorld(), 64, 2, 32), new Location(getWorld(), 133, 2, 86));
		startTimers();
	}
	
	private void startTimers() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(guardians.getEntitiesInCuboid(Guardian.class).size() < 50) {
					Guardian guardian = (Guardian) getWorld().spawnEntity(guardians.getRandomGuardianSpawn(), EntityType.GUARDIAN);
					guardian.setSilent(true);
					guardian.setMaximumAir(Integer.MAX_VALUE);
					guardian.setRemainingAir(Integer.MAX_VALUE);
				}
			}
		}.runTaskTimer(getPlugin(), 0, 5);
	}

	@Override
	public String getWarpTrigger() {
		return "parkour";
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(health.inArea(p.getLocation())) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 10, true));
		}
		
		if(checkpoint.inArea(p.getLocation())) {
			setPlayerRespawnLocation(p, new Location(getWorld(), 109, 5, 55));
		}
	}

}
