package org.golde.puzzlesafari.eventhandler;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.golde.puzzlesafari.Main;
import org.golde.puzzlesafari.utils.TabListUtil;
import org.golde.puzzlesafari.utils.WarpManager;

public class EventHandlerMiscWorldEvents extends EventHandlerBase {

	private final Runtime runtime = Runtime.getRuntime();
	private final int MB = 1048576;
	
	@Override
	public void onEnable() {
		startTabListTimer();
	}

	//Never worry about eating
	@EventHandler
	public void noHunger(FoodLevelChangeEvent e) {
		e.setFoodLevel(20);
		e.setCancelled(true);
	}

	//No rain
	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e) {
		e.setCancelled(true);
	}

	//No ice melting
	@EventHandler
	public void onBlockFade(final BlockFadeEvent event) {
		if ((event.getBlock().getType() == Material.ICE || event.getBlock().getType() == Material.SNOW || event.getBlock().getType() == Material.SNOW_BLOCK)) {
			event.setCancelled(true);
		}
	}

	//No leaf decay
	@EventHandler
	public void onLeavesDecay(final LeavesDecayEvent event) {
		event.setCancelled(true);
	}

	//TPS in the tab list
	private void startTabListTimer() {
		new BukkitRunnable() {

			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {

					final double[] tps = Bukkit.getTPS();
					final String[] tpsAvg = new String[tps.length];
					for (int i = 0; i < tps.length; ++i) {
						tpsAvg[i] = format(tps[i]);
					}



					TabListUtil.sendTablist(
							p, 
							ChatColor.YELLOW + "" + ChatColor.BOLD + "Puzzle Safari 2020", 
							ChatColor.GOLD + "Server TPS: " + StringUtils.join(tpsAvg, ", ") + "\n"
							+ ChatColor.AQUA + "Server Ram: " + ((runtime.totalMemory() - runtime.freeMemory()) / MB) + "MB / " + (runtime.totalMemory() / MB) + "MB"
							
							);
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 20 * 5);
	}

	//Can't take off armor
	@EventHandler
    public void onClick(InventoryClickEvent event) {
        if(event.getSlotType() == InventoryType.SlotType.ARMOR && event.getWhoClicked().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }
	
	//Players can't damage eachother
	@EventHandler
	public void onEntityDamagedByEntity(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			e.setCancelled(true);
		}
	}
	
	//No survival block breaking 4 u!
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(e.getPlayer().getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onHangingEntityBreak(HangingBreakEvent e) {
		if(e.getCause() != RemoveCause.ENTITY) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if(e.getPlayer().getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityBrokenByEntity(HangingBreakByEntityEvent e) {
		if(!(e.getRemover() instanceof Player && ((Player)e.getRemover()).getGameMode() == GameMode.CREATIVE)) {
			e.setCancelled(true);
		}
	}
	
	private static String format(final double tps) {
		return ((tps > 18.0) ? ChatColor.GREEN : ((tps > 16.0) ? ChatColor.YELLOW : ChatColor.RED)).toString() + ((tps > 21.0) ? "*" : "") + Math.min(Math.round(tps * 100.0) / 100.0, 20.0);
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		
		final Player p = e.getPlayer();
		final String lastWarp = WarpManager.getLastWarp(p);
		System.out.println("Respawn loc: " + lastWarp);
		e.setRespawnLocation(WarpManager.getWarp(lastWarp));
		
		new BukkitRunnable() {

			@Override
			public void run() {
				
				
				if(lastWarp != null) {
					WarpManager.warpPlayer(p, lastWarp);
				}
			}
			
		}.runTaskLater(getPlugin(), 2);
		
		
	}

}
