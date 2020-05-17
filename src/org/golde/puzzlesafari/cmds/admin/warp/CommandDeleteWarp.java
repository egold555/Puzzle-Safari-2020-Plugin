package org.golde.puzzlesafari.cmds.admin.warp;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.golde.puzzlesafari.cmds.base.AdminCommand;

public class CommandDeleteWarp extends AdminCommand {

	@Override
	public void execute2(Player sender, String[] args) {
		final File file = new File("plugins/PuzzleSafari2020", "warps.yml");
		final FileConfiguration cfg = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
		if (args.length == 1) {
			final String warpName = args[0].toLowerCase();
			cfg.set("warps." + warpName, (Object)null);
			sender.sendMessage(color("&6" + warpName + " &7was successfully deleted!"));
			try {
				cfg.save(file);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			sender.sendMessage(color("&6/delwarp <name>"));
		}
	}

}
