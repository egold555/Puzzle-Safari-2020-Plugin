package org.golde.puzzlesafari.challenges;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.golde.puzzlesafari.utils.cuboid.EndCuboid;
import org.golde.puzzlesafari.utils.cuboid.EndCuboid.EndCuboidCallback;

public class ChallengeSkydiving extends Challenge {

	@Override
	public void onEnable() {
		
		EndCuboid end = new EndCuboid(new Location(getWorld(), -3247, 65, -3212), new Location(getWorld(), -3188, 83, -3267), new EndCuboidCallback() {

			@Override
			public void onEnter(Player p) {
				sendFinishMessage(p, "windmill and the lake");
			}
		});
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				end.drawDebugParticles();
			}
		}.runTaskTimer(getPlugin(), 0, 1);
		
	}
	
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
				"Get to the village", 
				MOVEMENT_WASD_ELYTRA,
				"&oUse your mouse to change your direction / speed"
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
