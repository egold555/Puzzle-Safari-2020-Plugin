package org.golde.puzzlesafari.feature;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.golde.puzzlesafari.Main;
import org.golde.puzzlesafari.constants.TheGridSFX;
import org.golde.puzzlesafari.utils.ChatUtil;

public abstract class FeatureBase implements Listener {


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

	public String getWarpTrigger() {
		return null;
	}

	public void onEnter(Player p) {

	}

	public void reset(Player p) {

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

	public static final String MOVEMENT_WASD_RIGHT_CLICK = "Use &bWASD&f to move, &bRight Click &fto ";
	public static final String MOVEMENT_WASD = "Use &bWASD&f to move.";
	public static final String MOVEMENT_WASD_JUMP = "Use &bWASD&f to move, &bSpace&f to jump.";
	public static final String MOVEMENT_WASD_ATTACK = "Use &bWASD&f to move, &bSpace&f to jump, &bLeft Click&f to attack.";
	public static final String MOVEMENT_WASD_BOW = "Use &bWASD&f to move, &bSpace&f to jump, &bRight Click&f to draw back.";
	public static final String MOVEMENT_WASD_ELYTRA = "Use &bWASD&f to move, &bSpace&f to deploy your wings.";
	
	protected void sendEnterMessage(Player p, String title, String desc, String goal, String movement) {
		sendEnterMessage(p, title, desc, goal, movement, null);
	}

	protected void sendEnterMessage(Player p, String title, String desc, String goal, String movement, String extra) {
		p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.8f, 1.0f);
		ChatUtil.sendCentredMessage(p, "&c&m" + StringUtils.repeat(" ", 80));

		ChatUtil.sendCentredMessage(p, "&e&l" + title);
		p.sendMessage("");
		ChatUtil.sendCentredMessage(p, desc);
		ChatUtil.sendCentredMessage(p, "&6Your goal: " + goal);
		p.sendMessage("");
		ChatUtil.sendCentredMessage(p, movement);
		if(extra != null) {
			ChatUtil.sendCentredMessage(p, extra);
		}


		ChatUtil.sendCentredMessage(p, "&c&m" + StringUtils.repeat(" ", 80));
	}

	protected void sendFinishMessage(Player p, String title, String pictureOf) {
		//p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.8f, 0.1f);
		p.playSound(p.getLocation(), TheGridSFX.COMPLETE_CHALLENGE, SoundCategory.AMBIENT, 1, 1);
		ChatUtil.sendCentredMessage(p, "&c&m" + StringUtils.repeat(" ", 80));

		ChatUtil.sendCentredMessage(p, "&e&l" + title);
		p.sendMessage("");
		ChatUtil.sendCentredMessage(p, "Congrats! You finished the task!");
		p.sendMessage("");
		ChatUtil.sendCentredMessage(p, "&6Take a picture of &a" + pictureOf + " &6and submit it!");

		ChatUtil.sendCentredMessage(p, "&c&m" + StringUtils.repeat(" ", 80));
	}

}
