package org.golde.puzzlesafari.feature.zombie;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.golde.puzzlesafari.utils.Cuboid;

public class ZombieCuboid extends Cuboid {

	private Random random = new Random();
	
	public ZombieCuboid(Location loc1, Location loc2) {
		super(loc1, loc2);
	}
	
	public Location getRandomZombieSpawn() {
		
		World world = Bukkit.getWorld("world");
		
		Material mat = Material.BEDROCK; //TEMP
		int x = 0, y = 4, z = 0;
		
		while(mat != Material.AIR) {
			x = getRandom(loc1.getBlockX(), loc2.getBlockX());
			z = getRandom(loc1.getBlockZ(), loc2.getBlockZ());
			mat = world.getBlockAt(x, y, z).getType();
		}
		
		return new Location(world, x, y, z);
	}

	private int getRandom(int from, int to) {
	    if (from < to)
	        return from + random.nextInt(Math.abs(to - from));
	    return from - random.nextInt(Math.abs(to - from));
	}
	
}
