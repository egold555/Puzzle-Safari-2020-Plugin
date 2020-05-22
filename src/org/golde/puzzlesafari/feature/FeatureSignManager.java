package org.golde.puzzlesafari.feature;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.golde.puzzlesafari.utils.WarpManager;

public class FeatureSignManager extends FeatureBase {

	@Override
	public void onEnable() {
	}
	
	@EventHandler
	public void onEdit(SignChangeEvent sign) {

		if(sign.getLine(0).equalsIgnoreCase(">done")) {
			sign.setLine(0, "");
			sign.setLine(1, "Click me to return");
			sign.setLine(2, "to the main lab!");
			return;
		}
		
		if(sign.getLine(0).equalsIgnoreCase(">warp")) {
			
			String warpName = sign.getLine(1).toLowerCase();
			if(WarpManager.warpExists(warpName)) {
				sign.setLine(0, ChatColor.DARK_BLUE + "[Warp]");
				sign.setLine(1, warpName);
			}
			else {
				sign.setLine(0, ChatColor.RED + "[Warp]");
				sign.setLine(1, "Warp does not");
				sign.setLine(2, "exist!");
			}
			
			return;
		}
		
		if(sign.getLine(0).equalsIgnoreCase(">back")) {
			sign.setLine(0, "");
			sign.setLine(1, "Click me");
			sign.setLine(2, "to go back!");
			return;
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		Block block = e.getClickedBlock();
		
		if(block == null) {
			return;
		}
		
		if(e.getHand() != EquipmentSlot.HAND) {
			return;
		}
		
		if(block.getState() instanceof Sign) {
			Sign sign = (Sign) block.getState();
			
			if(e.getAction() == Action.LEFT_CLICK_BLOCK && player.getGameMode() == GameMode.CREATIVE) {
				return;
			}
			
			if((sign.getLine(1).equals("Click me to return") && sign.getLine(2).equals("to the main lab!")) || (sign.getLine(1).equals("Click me") && sign.getLine(2).equals("to go back!"))) {
				WarpManager.warpPlayer(player, "spawn");
			}
			
			if(sign.getLine(0).equals(ChatColor.DARK_BLUE + "[Warp]")) {
				String warpName = sign.getLine(1);
				Location warp = WarpManager.getWarp(warpName);
				if(warp != null) {
					WarpManager.warpPlayer(player, warpName);			
				}
				else {
					sign.setLine(0, ChatColor.DARK_RED + "[Warp]");
					player.sendMessage(ChatColor.RED + "[!] Something has gone wrong. Please contact an Administrator");
				}
			}
			
		}

	}

}
