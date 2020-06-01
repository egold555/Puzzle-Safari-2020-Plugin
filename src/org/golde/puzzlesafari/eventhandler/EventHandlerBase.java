package org.golde.puzzlesafari.eventhandler;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.golde.puzzlesafari.Main;

public abstract class EventHandlerBase implements Listener {


	public final void onInternalEnable() {
		Bukkit.getPluginManager().registerEvents(this, getPlugin());
		onEnable();
	}
	public void onEnable() {}
	public void onDisable() {};

	protected final Main getPlugin() {
		return Main.getInstance();
	}

	protected final String color(String msg) {
		return getPlugin().color(msg);
	}

	protected final World getWorld() {
		return Bukkit.getWorld("world");
	}
	
	public final void reset(Player p) {

		//clear inventory
		p.getInventory().clear();

		//clear potion effects
		for (PotionEffect effect : p.getActivePotionEffects()) {
			p.removePotionEffect(effect.getType());
		}		

		p.updateInventory();

		//add regen
		p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*3, 10, true));

		//reset time
		p.resetPlayerTime();
	}

	

}
