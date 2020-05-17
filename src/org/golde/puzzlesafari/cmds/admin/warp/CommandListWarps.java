package org.golde.puzzlesafari.cmds.admin.warp;

import java.io.File;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.golde.puzzlesafari.cmds.base.AdminCommand;

public class CommandListWarps extends AdminCommand {

	@Override
	public void execute2(Player p, String[] args) {
        final File file = new File("plugins/PuzzleSafari2020", "warps.yml");
        final FileConfiguration cfg = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
        if (cfg.isSet("warps")) {
            final Set<String> set = (Set<String>)cfg.getConfigurationSection("warps").getKeys(false);
            final String message = String.join(", ", set);
            p.sendMessage(color("&6&lWarps: &b" + message));
        }
        else {
            p.sendMessage(color("&cNo warps found."));
        }
	}

}
