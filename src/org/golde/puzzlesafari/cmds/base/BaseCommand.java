package org.golde.puzzlesafari.cmds.base;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.golde.puzzlesafari.Main;

import com.sun.istack.internal.NotNull;

public abstract class BaseCommand implements CommandExecutor {

	@Override
	public final boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2, @NotNull String[] args) {
		execute(sender, args);
		return true;
	}
	
	public abstract void execute(CommandSender sender, String[] args);

	protected final void errorMessage(CommandSender sender, String msg) {
		sender.sendMessage(ChatColor.RED + "[!] " + msg);
	}
	
	protected final static String color(String msg) {
		return Main.getInstance().color(msg);
	}
	
}
