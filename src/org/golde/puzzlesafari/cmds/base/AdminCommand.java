package org.golde.puzzlesafari.cmds.base;

import org.bukkit.entity.Player;

public abstract class AdminCommand extends PlayerCommand {

	@Override
	public void execute(Player sender, String[] args) {
		
		if(sender.isOp()) {
			execute2(sender, args);
		}
		else {
			errorMessage(sender, "You do not have permission to use this command.");
		}
		
	}

	public abstract void execute2(Player sender, String[] args);

}
