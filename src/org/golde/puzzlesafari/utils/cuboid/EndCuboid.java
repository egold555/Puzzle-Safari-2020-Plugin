package org.golde.puzzlesafari.utils.cuboid;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.golde.puzzlesafari.eventhandler.EventHandlerBase;

public class EndCuboid extends Cuboid {

	public interface EndCuboidCallback {
		public void onEnter(Player p);
	}

	private static HashMap<EndCuboid, Set<UUID>> cuboids = new HashMap<EndCuboid, Set<UUID>>();

	private final EndCuboidCallback callback;

	public EndCuboid(Location loc1, Location loc2, EndCuboidCallback callback) {
		super(loc1, loc2);
		cuboids.put(this, new HashSet<UUID>());
		this.callback = callback;
	}


	public static class EventHandlerEndCuboidChecker extends EventHandlerBase {

		public void onEnable() {


			new BukkitRunnable() {

				@Override
				public void run() {
					check();
				}

			}.runTaskTimer(getPlugin(), 0, 1);



		}
		
		@Override
		public void onDisable() {
			cuboids.clear();
		}

		private void check() {
			for(EndCuboid c : cuboids.keySet()) {

				HashSet<UUID> playersInArea = new HashSet<UUID>();

				for(Entity e : c.getEntitiesInCuboid(Player.class)) {

					if(e instanceof Player) {
						Player p = (Player) e;
						playersInArea.add(p.getUniqueId());

					}

				}

				for(UUID uuid : playersInArea) {

					 
					if(!cuboids.get(c).contains(uuid)) {
						cuboids.get(c).add(uuid);
						Player p = Bukkit.getPlayer(uuid);
						c.callback.onEnter(p);
					}

				}

				for(UUID uuid : cuboids.get(c)) {
					if(!playersInArea.contains(uuid)) {
						cuboids.get(c).remove(uuid);
					}
				}

			}
		}

	}

}
