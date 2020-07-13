package org.golde.puzzlesafari.cmds.admin;

import org.bukkit.entity.Player;
import org.golde.puzzlesafari.cmds.base.AdminCommand;

public class CommandTest extends AdminCommand {

	@Override
	public void execute2(Player sender, String[] args) {
		
		sender.sendMessage("Success.");
	}

}
