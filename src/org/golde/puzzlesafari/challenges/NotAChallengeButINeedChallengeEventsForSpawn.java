package org.golde.puzzlesafari.challenges;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.golde.puzzlesafari.eventhandler.EventHandlerBase;
import org.golde.puzzlesafari.utils.WarpManager;
import org.golde.puzzlesafari.utils.cuboid.Cuboid;

public class NotAChallengeButINeedChallengeEventsForSpawn extends Challenge {

	private Cuboid lobby;

	private static Set<Material> blockedMaterials = new HashSet<Material>();

	static {
		blockedMaterials.clear();
		blockedMaterials.add(Material.BEACON);
		blockedMaterials.add(Material.HOPPER);
		blockedMaterials.add(Material.BREWING_STAND);
		blockedMaterials.add(Material.LEVER);
		blockedMaterials.add(Material.FENCE_GATE);
	}

	@Override
	public void onEnable() {
		lobby = new Cuboid(new Location(getWorld(), -73, 30, -87), new Location(getWorld(), -111, 1, -32));
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		reset(p);
		WarpManager.warpPlayer(p, "spawn");
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		String msg = e.getMessage().toLowerCase().trim();
		Player p = e.getPlayer();
		if(msg.equals("quit") || msg.equals("end")) {
			new BukkitRunnable() {

				@Override
				public void run() {
					reset(p);
					WarpManager.warpPlayer(p, "spawn");
				}
			}.runTask(getPlugin());
		}
	}



	@EventHandler
	public void onBlockClient(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(p.getGameMode() != GameMode.SURVIVAL) {
			return;
		}
		if(lobby.inArea(p.getLocation())) {
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if(blockedMaterials.contains(e.getClickedBlock().getType())) {
					e.setCancelled(true);
					e.setUseInteractedBlock(Result.DENY);
				}
			}
		}


	}

	@EventHandler
	public void playerDamageEvent(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player)e.getEntity();
			if(p.getGameMode() != GameMode.SURVIVAL) {
				return;
			}
			if(lobby.inArea(p.getLocation())) {
				e.setCancelled(true);
			}
		}
	}

	@Override
	public String getWarpTrigger() {
		return "spawn";
	}

	@Override
	public void onEnter(Player p) {
		reset(p);
	}

	//unused
	@Override
	public String getTitle() {
		return null;
	}

}
