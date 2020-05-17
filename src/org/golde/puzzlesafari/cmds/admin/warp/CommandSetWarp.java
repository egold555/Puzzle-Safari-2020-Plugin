package org.golde.puzzlesafari.cmds.admin.warp;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.golde.puzzlesafari.cmds.base.AdminCommand;

public class CommandSetWarp extends AdminCommand {

	@Override
	public void execute2(Player p, String[] args) {
		final File file = new File("plugins/PuzzleSafari2020", "warps.yml");
		final FileConfiguration cfg = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
		final Location loc = p.getLocation();
		
		if (args.length == 1) {
			final String warpName = args[0].toLowerCase();
			cfg.set("warps." + warpName + ".world", (Object)loc.getWorld().getName());
			cfg.set("warps." + warpName + ".x", (Object)loc.getX());
			cfg.set("warps." + warpName + ".y", (Object)loc.getY());
			cfg.set("warps." + warpName + ".z", (Object)loc.getZ());
			cfg.set("warps." + warpName + ".yaw", (Object)loc.getYaw());
			cfg.set("warps." + warpName + ".pitch", (Object)loc.getPitch());
			try {
				cfg.save(file);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			p.sendMessage(color("&6" + warpName + " &7was created!"));
		}
		else {
			p.sendMessage(color("&6/setwarp <name>"));
		}
	}
}
