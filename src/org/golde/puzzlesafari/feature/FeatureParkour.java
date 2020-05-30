package org.golde.puzzlesafari.feature;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.golde.puzzlesafari.utils.WarpManager;
import org.golde.puzzlesafari.utils.cuboid.Cuboid;
import org.golde.puzzlesafari.utils.cuboid.EndCuboid;
import org.golde.puzzlesafari.utils.cuboid.EndCuboid.EndCuboidCallback;
import org.golde.puzzlesafari.utils.cuboid.MobCuboid;

public class FeatureParkour extends FeatureBase {

	private Cuboid checkpoint;
	private MobCuboid guardians;

	private static final int SPAWN_TICKS = 5;
	private static final int AMOUNT_OF_GUARDIANS = 50;
	private static final int CLEAR_TICKS = 20 * 60 * 15;

	@Override
	public void onEnable() {
		
		checkpoint = new Cuboid(new Location(getWorld(), 113, 4, 32), new Location(getWorld(), 118, 24, 86));
		guardians = new MobCuboid(new Location(getWorld(), 64, 2, 32), new Location(getWorld(), 133, 2, 86));

		new EndCuboid(new Location(getWorld(), 135, 6, 56), new Location(getWorld(), 136, 6, 57), new EndCuboidCallback() {

			@Override
			public void onEnter(Player p) {
				sendFinishMessage(p, "Parkour", "the white wall");
			}
		});

		startTimers();


	}

	private void startTimers() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if(guardians.getEntitiesInCuboid(Guardian.class).size() < AMOUNT_OF_GUARDIANS) {
					Guardian guardian = (Guardian) getWorld().spawnEntity(guardians.getRandomGuardianSpawn(), EntityType.GUARDIAN);
					guardian.setSilent(true);
					guardian.setMaximumAir(Integer.MAX_VALUE);
					guardian.setRemainingAir(Integer.MAX_VALUE);
				}
			}
		}.runTaskTimer(getPlugin(), 0, SPAWN_TICKS);

		new BukkitRunnable() {

			@Override
			public void run() {

				for(Entity z : guardians.getEntitiesInCuboid(Zombie.class)) {
					((Zombie)z).damage(100);
				}

			}
		}.runTaskTimer(getPlugin(), 0, CLEAR_TICKS);

	}

	@Override
	public String getWarpTrigger() {
		return "parkour";
	}

	@Override
	public void onEnter(Player p) {
		sendEnterMessage(
				p, 
				"Parkour",
				"It's just a hop skip and a jump away!", 
				"To get to the exit.",
				MOVEMENT_WASD
				);
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(checkpoint.inArea(p.getLocation())) {
			WarpManager.setCheckpoint(p, "parkour2");
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onDoorInteract(PlayerInteractEvent e) {

		if(e.getPlayer().getGameMode() != GameMode.CREATIVE) {
			if (e.getClickedBlock().getType() == Material.BIRCH_DOOR) {
				e.setCancelled(true);
			}
		}
		

	}

}
