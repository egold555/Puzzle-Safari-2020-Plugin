package org.golde.puzzlesafari.challenges;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.golde.puzzlesafari.utils.cuboid.EndCuboid;
import org.golde.puzzlesafari.utils.cuboid.EndCuboid.EndCuboidCallback;

public class ChallengeSkydiving extends Challenge {

	private EndCuboid end;
	
	@Override
	public void onEnable() {
		
		end = new EndCuboid(new Location(getWorld(), -3265, 68, -3295), new Location(getWorld(), -3075, 50, -3132), new EndCuboidCallback() {

			@Override
			public void onEnter(Player p) {
				sendFinishMessage(p, "windmill and the lake");
			}
		});
		
		//Reminder for everyone while flying that you can restart
		new BukkitRunnable() {
			
			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(p.getInventory() != null && p.getInventory().getChestplate() != null && p.getInventory().getChestplate().getType() == Material.ELYTRA) {
						if(p.getRemainingAir() != p.getMaximumAir()) {
							//underwater
							p.sendActionBar("Hold space to swim!");
						}
						else {
							p.sendActionBar(color("&eIf you get stuck, type &drestart&e to restart the challenge."));
						}
						
					}
				}
			}
		}.runTaskTimer(getPlugin(), 0, 20);
		
	}
	
//	@EventHandler
//	public void onPlayerAirChange(EntityAirChangeEvent e) {
//		
//		if(e.getEntityType() == EntityType.PLAYER) {
//			
//			Player p = (Player)e.getEntity();
//			if(end.inArea(p.getLocation())) {
//				p.sendActionBar("Hold space to swim");
//			}
//			
//		}
//		
//	}
	
	@Override
	public String getWarpTrigger() {
		return "skydiving";
	}
	
	@Override
	public String getTitle() {
		return "Skydiving";
	}
	
	@Override
	public void onEnter(Player p) {
		
		sendEnterMessage(
				p, 
				"Just don't look down alright!", 
				"Get to the village without dying", 
				MOVEMENT_WASD_ELYTRA
				);
		
		PlayerInventory inv = p.getInventory();
		
		ItemStack chestplate = new ItemStack(Material.ELYTRA);
		ItemMeta chestplateMeta = chestplate.getItemMeta();
		chestplateMeta.setUnbreakable(true);
		chestplate.setItemMeta(chestplateMeta);
		
		inv.setChestplate(chestplate);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				p.setGliding(true);
			}
			
		}.runTaskLater(getPlugin(), 5);
		
		
		p.updateInventory();
		
	}
	
}
