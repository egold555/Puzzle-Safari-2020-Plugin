package org.golde.puzzlesafari.feature;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.golde.puzzlesafari.utils.Cuboid;
import org.golde.puzzlesafari.utils.MobCuboid;

public class FeatureZombieKill extends FeatureBase {

	private MobCuboid cuboid;
	private Cuboid zombieKillCuboid;
	
	private static final int SPAWN_TICKS = 5;
	private static final int AMOUNT_OF_ZOMBIES = 100;
	private static final int CLEAR_TICKS = 20 * 60 * 15;

	@Override
	public void onEnable() {

		Location loc1 = new Location(getWorld(), 82, 4, -80);
		Location loc2 = new Location(getWorld(), 230, 4, -209);
		cuboid = new MobCuboid(loc1, loc2);

		loc1 = new Location(getWorld(), 197, 11, -127);
		loc2 = new Location(getWorld(), 197, 9, -125);
		zombieKillCuboid = new Cuboid(loc1, loc2);
		
		
		startTimers();
	}
	
	@Override
	public String getWarpTrigger() {
		return "zombie";
	}

	@Override
	public void onEnter(Player p) {
		p.setPlayerTime(20000, false);
		
		PlayerInventory inv = p.getInventory();
		
		ItemStack is = new ItemStack(Material.IRON_SWORD);
		ItemMeta im = is.getItemMeta();
		im.setUnbreakable(true);
		im.setDisplayName(ChatColor.YELLOW + "Zombie Slasher");
		is.setItemMeta(im);
		
		inv.setItemInMainHand(is);
		
		p.updateInventory();
		
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