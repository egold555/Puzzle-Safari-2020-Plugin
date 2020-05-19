package org.golde.puzzlesafari.utils;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.golde.puzzlesafari.Main;

public class WarpManager {

	private static final File file = new File("plugins/PuzzleSafari2020", "warps.yml");
	private static final FileConfiguration cfg = (FileConfiguration)YamlConfiguration.loadConfiguration(file);

	public static Location getWarp(String warpName) {

		if (!cfg.isSet("warps." + warpName)) {
			return null;
		}

		final World world = Bukkit.getWorld(cfg.getString("warps." + warpName + ".world"));
		final double x = cfg.getDouble("warps." + warpName + ".x");
		final double y = cfg.getDouble("warps." + warpName + ".y");
		final double z = cfg.getDouble("warps." + warpName + ".z");
		final float yaw = (float)cfg.getDouble("warps." + warpName + ".yaw");
		final float pitch = (float)cfg.getDouble("warps." + warpName + ".pitch");
		return new Location(world, x, y, z, yaw, pitch);
	}
	
	public static boolean warpExists(String warpName) {
		return getWarp(warpName) != null;
	}
	
	public static boolean warpPlayerSelf(Player p, String warpName) {
		return warpPlayer(p, warpName, null);
	}

	public static boolean warpPlayer(Player p, String warpName) {
		return warpPlayer(p, warpName, Bukkit.getConsoleSender());
	}

	public static boolean warpPlayer(Player p, String warpName, CommandSender sender) {

		Location warp = getWarp(warpName);
		if(warp == null) {
			return false;
		}

		p.teleport(getWarp(warpName));
		//p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 0.0f);
		
//		if(sender != null) {
//			p.sendMessage(Main.getInstance().color("&6" + sender.getName() + " &7teleported you to &6" + warpName));
//		}
//		else {
//			p.sendMessage(Main.getInstance().color("&7you have been teleported to &6" + warpName));
//		}
		
		Main.getInstance().callFeatureEnterFunction(p, warpName);
		
		return true;
	}
	
	public static void setWarp(String warpName, Location loc) {
		cfg.set("warps." + warpName + ".world", loc.getWorld().getName());
		cfg.set("warps." + warpName + ".x", loc.getX());
		cfg.set("warps." + warpName + ".y", loc.getY());
		cfg.set("warps." + warpName + ".z", loc.getZ());
		cfg.set("warps." + warpName + ".yaw", loc.getYaw());
		cfg.set("warps." + warpName + ".pitch", loc.getPitch());
		try {
			cfg.save(file);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteWarp(String warpName) {
		cfg.set("warps." + warpName, null);
		
		try {
			cfg.save(file);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Set<String> getWarps(){
		if (!cfg.isSet("warps")) {
			return null;
		}
		return cfg.getConfigurationSection("warps").getKeys(false);
	}

}
