package org.golde.puzzlesafari.cmds.admin.warp;

import org.bukkit.entity.Player;
import org.golde.puzzlesafari.cmds.base.AdminCommand;
import org.golde.puzzlesafari.utils.WarpManager;

public class CommandDeleteWarp extends AdminCommand {

	@Override
	public void execute2(Player sender, String[] args) {
		
		if (args.length == 1) {
			final String warpName = args[0].toLowerCase();
			WarpManager.deleteWarp(warpName);
			sender.sendMessage(color("&6" + warpName + " &7was successfully deleted!"));
			
		}
		else {
			sender.sendMessage(color("&6/delwarp <name>"));
		}
	}

}
