package org.golde.puzzlesafari.cmds.base;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class PlayerCommand extends BaseCommand {

	@Override
	public final void execute(CommandSender sender, String[] args) {
		if(sender instanceof Player) {
			execute((Player)sender, args);
		}
		else {
			errorMessage(sender, "Command is only for players.");
		}
	}

	public abstract void execute(Player sender, String[] args);
	
}
