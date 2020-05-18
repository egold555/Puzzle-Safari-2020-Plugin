package org.golde.puzzlesafari.cmds.admin.warp;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.golde.puzzlesafari.cmds.base.AdminCommand;
import org.golde.puzzlesafari.utils.WarpManager;

public class CommandSetWarp extends AdminCommand {

	@Override
	public void execute2(Player p, String[] args) {
		final Location loc = p.getLocation();

		if (args.length == 1) {
			String warpName = args[0].toLowerCase();
			WarpManager.setWarp(warpName, loc);
			p.sendMessage(color("&6" + warpName + " &7was created!"));
		}
		else {
			p.sendMessage(color("&6/setwarp <name>"));
		}
	}
}
