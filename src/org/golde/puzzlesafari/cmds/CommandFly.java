package org.golde.puzzlesafari.cmds;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.golde.puzzlesafari.cmds.base.PlayerCommand;

public class CommandFly extends PlayerCommand {

	private HashMap<UUID, Boolean> playerStates = new HashMap<UUID, Boolean>();
	
	@Override
	public void execute(Player sender, String[] args) {
		
		if(!playerStates.containsKey(sender.getUniqueId())) {
			playerStates.put(sender.getUniqueId(), false);
		}
		
		if(playerStates.get(sender.getUniqueId())) {
			
			//no flying
			sender.sendMessage("Fly mode has been disabled.");
			sender.setGameMode(GameMode.SURVIVAL);
			sender.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 15*20, 10));
			
		}
		else {
			sender.sendMessage("Fly mode has been enabled. Use your scroll wheel to change your flying speed");
			sender.setGameMode(GameMode.SPECTATOR);
		}
		
		playerStates.put(sender.getUniqueId(), !playerStates.get(sender.getUniqueId()));
		
	}

}
