package org.golde.puzzlesafari.cmds.admin.warp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.golde.puzzlesafari.cmds.base.AdminCommand;
import org.golde.puzzlesafari.utils.WarpManager;

public class CommandWarp extends AdminCommand {

	@Override
	public void execute2(Player p, String[] args) {
		
        if (args.length != 0) {
        	
        	final String warpName = args[0].toLowerCase();
        	
        	Location warp = WarpManager.getWarp(warpName);
        	
            if (warp != null) {
                
                if (args.length == 1) {
                     WarpManager.warpPlayerSelf(p, warpName);
                }
                else if (args.length == 2) {
                	final Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        WarpManager.warpPlayer(target, warpName, p);
                    }
                    else {
                        p.sendMessage(color("&cPlayer not found"));
                    }
                }
                else {
                    p.sendMessage(color("&6/warp <name>"));
                }
            }
            else {
                p.sendMessage(color("&6" + warpName + " &7does not exist!"));
            }
        }
        
	}
	
	
	
	

}
