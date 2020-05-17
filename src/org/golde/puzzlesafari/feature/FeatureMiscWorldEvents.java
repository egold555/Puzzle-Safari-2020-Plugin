package org.golde.puzzlesafari.feature;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.golde.puzzlesafari.Main;
import org.golde.puzzlesafari.utils.TabListUtil;

public class FeatureMiscWorldEvents extends FeatureBase {
	
	@Override
	public void onEnable() {
		
		this.registerEvents();
		
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

					
					
					TabListUtil.sendTablist(p, ChatColor.YELLOW + "" + ChatColor.BOLD + "Puzzle Safari 2020", ChatColor.GOLD + "Server Preformance: " + StringUtils.join(tpsAvg, ", "));
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 20 * 5);
	}

	private static String format(final double tps) {
		return ((tps > 18.0) ? ChatColor.GREEN : ((tps > 16.0) ? ChatColor.YELLOW : ChatColor.RED)).toString() + ((tps > 21.0) ? "*" : "") + Math.min(Math.round(tps * 100.0) / 100.0, 20.0);
	}

}
