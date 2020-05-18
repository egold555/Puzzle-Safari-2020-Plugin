package org.golde.puzzlesafari.cmds.admin.warp;

import java.util.Set;

import org.bukkit.entity.Player;
import org.golde.puzzlesafari.cmds.base.AdminCommand;
import org.golde.puzzlesafari.utils.WarpManager;

public class CommandListWarps extends AdminCommand {

	@Override
	public void execute2(Player p, String[] args) {
       
		Set<String> warps = WarpManager.getWarps();
		
        if (warps != null) {
           
            final String message = String.join(", ", warps);
            p.sendMessage(color("&6&lWarps: &b" + message));
        }
        else {
            p.sendMessage(color("&cNo warps found."));
        }
	}

}
