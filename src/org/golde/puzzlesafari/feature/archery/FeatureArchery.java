package org.golde.puzzlesafari.feature.archery;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.golde.puzzlesafari.feature.FeatureBase;
import org.golde.puzzlesafari.utils.NMSUtils;
import org.golde.puzzlesafari.utils.NMSUtils.Type;
import org.golde.puzzlesafari.utils.WarpManager;
import org.golde.puzzlesafari.utils.cuboid.Cuboid;
import org.golde.puzzlesafari.utils.cuboid.MobCuboid;

public class FeatureArchery extends FeatureBase {

	private HashMap<UUID, Integer> sheepHit = new HashMap<UUID, Integer>();
	private static final int MAX_SHEEP = 10;
	private static final int HOW_MANY_TO_HIT_TO_WIN = 5;
	private static final int CLEAR_TICKS = 20 * 60 * 15;
	private static final int SPAWN_TICKS = 20;
	
	private Cuboid playerArea;
	private MobCuboid sheepArea;
	
	@Override
	public void onEnable() {
		NMSUtils.registerEntity("custom_sheep", Type.SHEEP, CustomSheep.class, false);
		
		playerArea = new Cuboid(new Location(getWorld(), -178, 8, -249), new Location(getWorld(), -144, 11, -236));
		sheepArea = new MobCuboid(new Location(getWorld(), -178, 8, -251), new Location(getWorld(), -144, 10, -290));
		
		startTimer();
		
	}
	
	private void startTimer() {
		
		
		new BukkitRunnable() {

			@Override
			public void run() {
				
				//not sure why this doewn't obay mob spawning rules, but that seems to be a issue with custom sheep / net.minecraft.World
				if(playerArea.getEntitiesInCuboid(Player.class).size() > 0) {
					if(sheepArea.getEntitiesInCuboid(Sheep.class).size() < MAX_SHEEP) {
						spawnCustomSheep(sheepArea.getRandomSpawn(11));
					}
				}
				

			}
		}.runTaskTimer(getPlugin(), 10, SPAWN_TICKS);
		
	
		new BukkitRunnable() {

			@Override
			public void run() {
				
				for(Entity e : playerArea.getEntitiesInCuboid(Player.class)) {
					
					if(e instanceof Player) {
						Player p = (Player)e;
						
						if(!sheepHit.containsKey(p.getUniqueId())) {
							sheepHit.put(p.getUniqueId(), 0);
						}
						
						updateActionBar(p);
						
					}
					
				}
				
			}
			
		}.runTaskTimer(getPlugin(), 0, 20);
		
		new BukkitRunnable() {

			@Override
			public void run() {

				for(Entity z : sheepArea.getEntitiesInCuboid(Sheep.class)) {
					((Sheep)z).damage(100);
				}

			}
		}.runTaskTimer(getPlugin(), 0, CLEAR_TICKS);
		
	}
	
	private void updateActionBar(Player p) {
		p.sendActionBar('&', "&eSheep Hit: &b" + sheepHit.getOrDefault(p.getUniqueId(), 0) + "&f/&b" + HOW_MANY_TO_HIT_TO_WIN);
	}

	@Override
	public String getWarpTrigger() {
		return "sheep";
	}
	
	@Override
	public void onEnter(Player p) {
		sheepHit.put(p.getUniqueId(), 0);
		
		PlayerInventory inv = p.getInventory();
		
		
		ItemStack bow = new ItemStack(Material.BOW);
		ItemMeta bowMeta = bow.getItemMeta();
		
		bowMeta.setDisplayName(ChatColor.YELLOW + "Sheep Killer 9000");
		bowMeta.setUnbreakable(true);
		bowMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		
		bow.setItemMeta(bowMeta);
		inv.setItem(0, bow);
		
		
		ItemStack arrow = new ItemStack(Material.ARROW);
		ItemMeta arrowMeta = arrow.getItemMeta();
		
		arrowMeta.setDisplayName(ChatColor.YELLOW + "Infinite Arrow");
		
		arrow.setItemMeta(arrowMeta);
		inv.setItem(1, arrow);
		
		p.updateInventory();
		
		sendEnterMessage(
				p, 
				"Sheep Shenanigans",
				"Are ewe ready for this?", 
				"Kill " + HOW_MANY_TO_HIT_TO_WIN + " Sheep.", 
				MOVEMENT_WASD_ATTACK
				);
		
		
		p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 20));
		
	}
	
	@EventHandler
	public void projectileHitBlock(ProjectileHitEvent e) {
		
		if(e.getEntityType() != EntityType.ARROW) {
			return;
		}
		
		Arrow arrow = (Arrow)e.getEntity();
		
		if(!(arrow.getShooter() instanceof Player)) {
			return;
		}
		
		Player player = (Player)arrow.getShooter();
		
		if(e.getHitBlock() != null) {
			arrow.remove();
		}
		
		if(e.getHitEntity() instanceof Player) {
			arrow.spigot().setDamage(0);
		}
		
		if(e.getHitEntity() instanceof Sheep) {
			Sheep sheep = (Sheep) e.getHitEntity();
			sheep.damage(100); //kill the sheep
			arrow.remove();
			UUID playerUUID = player.getUniqueId();
			
			player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, SoundCategory.PLAYERS, 1, 1);
			int haveGottenNew = sheepHit.get(playerUUID) + 1;
	
			if(haveGottenNew >= HOW_MANY_TO_HIT_TO_WIN) {
				//win
				reset(player);
				WarpManager.warpPlayer(player, "sheepend");
				sendFinishMessage(player, "Sheep Shenanigans", "The giant sheep");
				
				sheepHit.put(playerUUID, 0);
			}
			else {
				sheepHit.put(playerUUID, haveGottenNew);
				
			}
			updateActionBar(player);
			
			
			
			
		}
		
	}
	
	private void spawnCustomSheep(Location loc) {
		World world = loc.getWorld();
		
		net.minecraft.server.v1_12_R1.WorldServer nmsWorld = ((CraftWorld)world).getHandle();
		CustomSheep sheep = new CustomSheep(nmsWorld);
		sheep.setPosition(loc.getX(), loc.getY(), loc.getZ());
		nmsWorld.addEntity(sheep);
	}
	
}
