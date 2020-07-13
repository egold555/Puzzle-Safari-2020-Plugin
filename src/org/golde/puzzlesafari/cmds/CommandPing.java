package org.golde.puzzlesafari.cmds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.golde.puzzlesafari.Main;
import org.golde.puzzlesafari.cmds.base.PlayerCommand;

public class CommandPing extends PlayerCommand {

	@Override
	public void execute(Player player, String[] args) {
		if (!(args.length == 1)) {
            int ping = ((CraftPlayer) player).getHandle().ping;
            player.sendMessage(Main.getInstance().color("&7Your ping is: " + colorPing(ping)) + "ms");
            return;
        }

        else if (args.length == 1)
        {

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                errorMessage(player, "Player &e" + ChatColor.stripColor(args[0]) + "&c not found.");
                return;
            }

            int ping = ((CraftPlayer) target).getHandle().ping;
            String targetName = target.getName();
            player.sendMessage(Main.getInstance().color("&e" + targetName + "&7's ping is " + colorPing(ping)) + "ms");
            return;
        }
	}
	
	static String colorPing(int ping) {
		ChatColor color = ChatColor.GREEN;
		if (ping >= 200 && ping <= 300) {
			color = ChatColor.YELLOW;
	    } else if (ping >= 301 && ping <= 499) {
	    	color = ChatColor.RED;
	    } else if (ping >= 500) {
	    	color = ChatColor.DARK_RED;
	    }
		return color + "" + ping;
	}

}
