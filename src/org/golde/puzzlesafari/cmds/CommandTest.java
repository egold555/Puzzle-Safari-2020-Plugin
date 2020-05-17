package org.golde.puzzlesafari.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.sun.istack.internal.NotNull;

public class CommandTest implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String idk, @NotNull String[] args) {
		sender.sendMessage("Hello " + sender.getName() + "!");
		return true;
	}

}
