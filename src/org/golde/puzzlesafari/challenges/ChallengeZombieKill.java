package org.golde.puzzlesafari.challenges;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.golde.puzzlesafari.constants.TheGridSFX;
import org.golde.puzzlesafari.utils.cuboid.Cuboid;
import org.golde.puzzlesafari.utils.cuboid.EndCuboid;
import org.golde.puzzlesafari.utils.cuboid.EndCuboid.EndCuboidCallback;
import org.golde.puzzlesafari.utils.cuboid.MobCuboid;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class ChallengeZombieKill extends Challenge {

	private MobCuboid cuboid;
	private Cuboid zombieKillCuboid;

	private static final int SPAWN_TICKS = 5;
	private static final int AMOUNT_OF_ZOMBIES = 100;
	private static final int CLEAR_TICKS = 20 * 60 * 15;

	private static final Random RANDOM = new Random();
	private static final String[] ZOMBIE_MASKS = {
			//"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDQyYjE2MmI0ZTJkMjNhYmE0OThlNWM1MTAyNzMxNTgyMTc0MjMyYmQxYWViMDc3OTA3OTEwM2ZkNDdlZGQ4YSJ9fX0=", //Steve mask
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQ5ZTJjNTllNjYzNDhhMjkxZjQ0N2ZiMjU5Y2JjZWNmYzkzNDc1ZDMyNmQwMTFmMjk0NWYyZGUwM2NlZDAifX19", //Miner
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzNmYjRlNWRiOTdmNDc5YzY2YTQyYmJkOGE3ZDc4MWRhZjIwMWE4ZGRhZjc3YWZjZjRhZWY4Nzc3OWFhOGI0In19fQ==", //Injured
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmFhYzIyMzAxNTlhODAzZDI4Y2ZkZTY2NjJlYWYzNzlkYTg5YThhMDczYzdiZTIwYzZlN2U0MDhkZDg4NjFkMSJ9fX0=", //Injured 2
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjczNTBmZDJkZjQzMGJjNGI5YWFlY2QxM2M1MWM5NTFlNWJmYzlmMTMzYmFkMGNkYWVmZTE5MjJhOWU0N2ZhMyJ9fX0=", //Injured villager
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmZmYTI0NzA5YWI0ZTRhYzA5OTk2ZGMzOWM0MjEzZTJkY2U4Yjk3YzU4ODEzZGMwNDM1ZDA2YWQxNjQ5YjJhNiJ9fX0=", //diver
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjA4ZDgzYTUwNWNiOTIyMGRiMjZlMGNlODcxODdjN2I0NmRjYzdiMWQxNzFlYzIzNzUyYmFiMGY3YTg3ZTJlNyJ9fX0=", //3d
			//"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTE5M2FjNTI3M2M5OGIzNzg2MTcyODljYWM2NDkwZTEzNDhiNWY2OTFhNzEwM2E1Y2QxY2YzZGVjMmU1YmE4NSJ9fX0=", //cleric
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDFjYTM0MjZkY2UxMTczZTA1ZTM2ZTY1Yzc1YjVmM2M3Mjk5YjY3NmU2ZmI1MzkxNzFhMzM1ZjMzZmI0MTYifX19", //king
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTZiZTViNDFkNjg5ZDdlYjUxNTYyZWIyYTQ1ZDcyMGI5ZDZkNmZiOTM4MmI1YjQyMjY2YzMxNDg3ODMxIn19fQ==", //jason
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDY5ZGIxODc2ODJkYTM3MDdhM2RiYzBhYzAzZGUxOGY2NzUyZDczODk5MjQ3NjEyMzZjM2I4NzBlYjkyMWM3OSJ9fX0=", //bloody
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmY5MzhhZTAzMTM4YjJkMDIyZDYyODA4NThmYzY3ZTI2MTIwODZlODExMmY5ZDE1NDY3NjQ1NjE5Nzg0MjViMSJ9fX0=", //cyborg
			//"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzNmN2NjZjYxZGJjM2Y5ZmU5YTYzMzNjZGUwYzBlMTQzOTllYjJlZWE3MWQzNGNmMjIzYjNhY2UyMjA1MSJ9fX0=", //drowned
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTg3MWQ5NTg3YTZlMTM5MGYxMTc4ZGYxMWU2YzQyMDc2OGQwZmIxZmQyMTIyNjQ2ZDBiMWVjN2NjZTYwNzQ0NSJ9fX0=", //hooded
			//"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2RjMmU3NTg1ZGQ5YTRmMTA3OTdlZjM1ODE1MTBjNmM3ZmM5Y2UyYWE5OWJlNWViNjA0YzdiOGUzZjc2NmI1NCJ9fX0=", //drowned bloody
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2FkMDU0MTI1MzQyNTRhZDBiOGVjYWU1ZTExMDhhODkxODA5M2IxZTFhYTllMTJlMmFlZWJkNmY3ZWVhMzFjMSJ9fX0=", //COVID19
			null, //normal no head
	};

	@Override
	public void onEnable() {

		Location loc1 = new Location(getWorld(), 82, 4, -80);
		Location loc2 = new Location(getWorld(), 230, 4, -209);
		cuboid = new MobCuboid(loc1, loc2);

		loc1 = new Location(getWorld(), 197, 11, -127);
		loc2 = new Location(getWorld(), 197, 9, -125);
		zombieKillCuboid = new Cuboid(loc1, loc2);

		new EndCuboid(loc1, loc2, new EndCuboidCallback() {

			@Override
			public void onEnter(Player p) {
				sendFinishMessage(p, "painting");
			}
		});


		startTimers();
	}

	@Override
	public String getWarpTrigger() {
		return "zombie";
	}

	@Override
	public String getTitle() {
		return "Escape the Zombies";
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

		sendEnterMessage(
				p, 
				"The zombies... are coming...", 
				"Get to your house.", 
				MOVEMENT_WASD_ATTACK,
				"&oZombies are one hit kill"
				);

		new BukkitRunnable() {

			@Override
			public void run() {

				p.playSound(p.getLocation(), TheGridSFX.ZOMBIES_ARE_COMING, SoundCategory.AMBIENT, 10000, 1);

			}
		}.runTaskLater(getPlugin(), 20 * 5);

	}

	private void startTimers() {

		//spawn zombies every 5 ticks or .2 seconds
		new BukkitRunnable() {

			@Override
			public void run() {

				if((cuboid.getEntitiesInCuboid(Zombie.class).size() + cuboid.getEntitiesInCuboid(Husk.class).size()) < AMOUNT_OF_ZOMBIES) {
					spawnAZombie();
				}

				//kill all chickens. There is a better way by denying them spawning but it broke other things.
				//Chickens spawn from chicken riding zombies
				for(Entity c : cuboid.getEntitiesInCuboid(Chicken.class)) {
					((Chicken)c).damage(100);
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
				
				for(Entity z : zombieKillCuboid.getEntitiesInCuboid(Husk.class)) {
					((Husk)z).damage(100);
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
				
				for(Entity z : zombieKillCuboid.getEntitiesInCuboid(Husk.class)) {
					((Husk)z).damage(100);
				}

			}
		}.runTaskTimer(getPlugin(), 0, CLEAR_TICKS);

	}

	@SuppressWarnings("deprecation")
	private void spawnAZombie() {

		if(RANDOM.nextBoolean()) {
			Location spawnLoc = cuboid.getRandomZombieSpawn();
			Zombie zombie = (Zombie) cuboid.getLoc1().getWorld().spawnEntity(spawnLoc, EntityType.ZOMBIE);
			zombie.setMaxHealth(3);
			zombie.setHealth(zombie.getMaxHealth()); //seems to be an issue with hearts not being filled?
			zombie.setBaby(false);
			zombie.setCanPickupItems(false);
			EntityEquipment ee = zombie.getEquipment();

			String base64 = ZOMBIE_MASKS[RANDOM.nextInt(ZOMBIE_MASKS.length)];
			if(base64 != null) {
				ee.setHelmet(getCustomTextureHead(base64));
			}
			else {
				ee.setHelmet(new ItemStack(Material.STONE_BUTTON)); 
			}
		}
		else {
			Location spawnLoc = cuboid.getRandomZombieSpawn();
			Husk zombie = (Husk) cuboid.getLoc1().getWorld().spawnEntity(spawnLoc, EntityType.HUSK);
			zombie.setMaxHealth(3);
			zombie.setHealth(zombie.getMaxHealth()); //seems to be an issue with hearts not being filled?
			zombie.setBaby(false);
			zombie.setCanPickupItems(false);
			EntityEquipment ee = zombie.getEquipment();
			ee.setHelmet(new ItemStack(Material.STONE_BUTTON)); 
			
		}


	}

	private static ItemStack getCustomTextureHead(String base64) {
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta meta = (SkullMeta) head.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), "");
		profile.getProperties().put("textures", new Property("textures", base64));
		Field profileField = null;
		try {
			profileField = meta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(meta, profile);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		head.setItemMeta(meta);
		return head;
	}

	//	@EventHandler
	//	public void onEntityCombust(EntityCombustEvent event){
	//
	//		Entity entity = event.getEntity();
	//
	//		if(entity instanceof Zombie){
	//			if(cuboid.inArea(entity.getLocation()))
	//				event.setCancelled(true);
	//
	//		}
	//
	//	} 

}
