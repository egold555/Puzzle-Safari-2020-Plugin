package org.golde.puzzlesafari.feature.zombie;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.golde.puzzlesafari.feature.FeatureBase;
import org.golde.puzzlesafari.utils.Cuboid;

public class FeatureZombieKill extends FeatureBase {

	private ZombieCuboid cuboid;
	private Cuboid zombieKillCuboid;
	
	private static final int SPAWN_TICKS = 5;
	private static final int AMOUNT_OF_ZOMBIES = 100;
	private static final int CLEAR_TICKS = 20 * 60 * 15;

	@Override
	public void onEnable() {

		registerEvents();

		Location loc1 = new Location(Bukkit.getWorld("world"), 82, 4, -80);
		Location loc2 = new Location(Bukkit.getWorld("world"), 230, 4, -209);
		cuboid = new ZombieCuboid(loc1, loc2);

		loc1 = new Location(Bukkit.getWorld("world"), 197, 11, -127);
		loc2 = new Location(Bukkit.getWorld("world"), 197, 9, -125);
		zombieKillCuboid = new Cuboid(loc1, loc2);
		
		
		startTimers();
	}

	private void startTimers() {

		//spawn zombies every 5 ticks or .2 seconds
		new BukkitRunnable() {

			@Override
			public void run() {

				if(cuboid.getEntitiesInCuboid(Zombie.class).size() < AMOUNT_OF_ZOMBIES) {
					spawnAZombie();
				}

			}
		}.runTaskTimer(getPlugin(), 0, SPAWN_TICKS);
		
		//No zombies can enter the house
		new BukkitRunnable() {

			@Override
			public void run() {

				for(Entity z : zombieKillCuboid.getEntitiesInCuboid(Zombie.class)) {
					((Zombie)z).damage(100);
				}

			}
		}.runTaskTimer(getPlugin(), 0, 1);
		
		//Kill all zombies every 15m just to reset things
		
		new BukkitRunnable() {

			@Override
			public void run() {

				for(Entity z : cuboid.getEntitiesInCuboid(Zombie.class)) {
					((Zombie)z).damage(100);
				}

			}
		}.runTaskTimer(getPlugin(), 0, CLEAR_TICKS);

	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(cuboid.inArea(p.getLocation())) {
			p.setPlayerTime(20000, false);
		}
		else if(!cuboid.inArea(p.getLocation())) {
			p.resetPlayerTime();
		}
	}



	private void spawnAZombie() {

		Location spawnLoc = cuboid.getRandomZombieSpawn();
		Zombie zombie = (Zombie) cuboid.getLoc1().getWorld().spawnEntity(spawnLoc, EntityType.ZOMBIE);
		zombie.setMaxHealth(3);
		zombie.setHealth(zombie.getMaxHealth()); //seems to be an issue with hearts not being filled?
		zombie.setBaby(false);
	}

	@EventHandler
	public void onEntityCombust(EntityCombustEvent event){

		Entity entity = event.getEntity();

		if(entity instanceof Zombie){
			if(cuboid.inArea(entity.getLocation()))
				event.setCancelled(true);

		}

	} 

}
