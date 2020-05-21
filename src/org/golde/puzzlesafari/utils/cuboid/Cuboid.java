package org.golde.puzzlesafari.utils.cuboid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Location;
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

	public List<Entity> getEntitiesInCuboid() {
		return getEntitiesInCuboid(null);
	}

	public List<Entity> getEntitiesInCuboid(Class<? extends Entity> type) {

		List<Entity> toReturn = new ArrayList<Entity>();

		World world = loc1.getWorld();

		Collection<? extends Entity> entities = (type != null ? world.getEntitiesByClass(type) : loc1.getWorld().getEntities());

		for(Entity e : entities) {
			if(inArea(e.getLocation())) {
				toReturn.add(e);
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


}
