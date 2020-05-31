package org.golde.puzzlesafari.utils.cuboid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class Cuboid {

	protected final Location loc1;
	protected final Location loc2;

	public Cuboid(Location loc1, Location loc2) {
		this.loc1 = loc1;
		this.loc2 = loc2;
	}

	public boolean inArea(Location targetLocation){
		if((targetLocation.getBlockX() >= loc1.getBlockX() && targetLocation.getBlockX() <= loc2.getBlockX()) || (targetLocation.getBlockX() <= loc1.getBlockX() && targetLocation.getBlockX() >= loc2.getBlockX())){
			if((targetLocation.getBlockZ() >= loc1.getBlockZ() && targetLocation.getBlockZ() <= loc2.getBlockZ()) || (targetLocation.getBlockZ() <= loc1.getBlockZ() && targetLocation.getBlockZ() >= loc2.getBlockZ())){
				if((targetLocation.getBlockY() >= loc1.getBlockY() && targetLocation.getBlockY() <= loc2.getBlockY()) || (targetLocation.getBlockY() <= loc1.getBlockY() && targetLocation.getBlockY() >= loc2.getBlockY())){
					return true;
				}
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public <T extends org.bukkit.entity.Entity> List<T> getEntitiesInCuboid(Class<T> type) {

		List<T> toReturn = new ArrayList<T>();

		World world = loc1.getWorld();

		Collection<? extends T> entities = (type != null ? world.getEntitiesByClass(type) : (Collection<T>) loc1.getWorld().getEntities());

		for(Entity e : entities) {
			if(inArea(e.getLocation())) {
				toReturn.add((T) e);
			}
		}

		return toReturn;
	}

	public final Location getLoc1() {
		return loc1;
	}

	public final Location getLoc2() {
		return loc2;
	}
	
	public void drawDebugParticles() {
		
		for(int x = Math.min(loc1.getBlockX(), loc2.getBlockX()); x <=  Math.max(loc1.getBlockX(), loc2.getBlockX()); x++) {
			for(int y =  Math.min(loc1.getBlockY(), loc2.getBlockY()); y <= Math.max(loc1.getBlockY(), loc2.getBlockY()); y++) {
				for(int z =  Math.min(loc1.getBlockZ(), loc2.getBlockZ()); z <= Math.max(loc1.getBlockZ(), loc2.getBlockZ()); z++) {
					loc1.getWorld().spawnParticle(Particle.REDSTONE, x + 0.5, y + 0.5, z + 0.5, 1, 0, 0, 0, 0);
					
				}
			}
		}
		
	}


}
