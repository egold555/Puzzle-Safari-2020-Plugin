package org.golde.puzzlesafari.challenges;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.golde.puzzlesafari.constants.TheGridSFX;
import org.golde.puzzlesafari.eventhandler.EventHandlerBase;
import org.golde.puzzlesafari.utils.ChatUtil;

public abstract class Challenge extends EventHandlerBase {

	public abstract String getWarpTrigger();

	public abstract void onEnter(Player p);
	
	public abstract String getTitle();

	public static final String MOVEMENT_WASD_RIGHT_CLICK = "Use &bWASD&f to move, &bRight Click &fto ";
	public static final String MOVEMENT_WASD = "Use &bWASD&f to move.";
	public static final String MOVEMENT_WASD_JUMP = "Use &bWASD&f to move, &bSpace&f to jump.";
	public static final String MOVEMENT_WASD_ATTACK = "Use &bWASD&f to move, &bSpace&f to jump, &bLeft Click&f to attack.";
	public static final String MOVEMENT_WASD_BOW = "Use &bWASD&f to move, &bSpace&f to jump, &bRight Click&f to draw back, release to shoot.";
	public static final String MOVEMENT_WASD_ELYTRA = "Use your &bmouse&f to change your &bdirection / speed&f.";
	
	protected final void sendEnterMessage(Player p, String desc, String goal, String movement) {
		sendEnterMessage(p, desc, goal, movement, null);
	}

	protected final void sendEnterMessage(Player p, String desc, String goal, String movement, String extra) {
		p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.8f, 1.0f);
		ChatUtil.sendCentredMessage(p, "&c&m" + StringUtils.repeat(" ", 80));

		ChatUtil.sendCentredMessage(p, "&e&l" + getTitle());
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

	protected final void sendFinishMessage(Player p, String pictureOf) {
		//p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.8f, 0.1f);
		p.playSound(p.getLocation(), TheGridSFX.COMPLETE_CHALLENGE, SoundCategory.AMBIENT, 10000, 1);
		ChatUtil.sendCentredMessage(p, "&c&m" + StringUtils.repeat(" ", 80));

		ChatUtil.sendCentredMessage(p, "&e&l" + getTitle());
		p.sendMessage("");
		ChatUtil.sendCentredMessage(p, "Congrats! You finished the task!");
		p.sendMessage("");
		ChatUtil.sendCentredMessage(p, "&6Take a picture of &athe " + pictureOf + " &6and submit it!");

		ChatUtil.sendCentredMessage(p, "&c&m" + StringUtils.repeat(" ", 80));
	}
	
}
