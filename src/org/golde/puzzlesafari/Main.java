package org.golde.puzzlesafari;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.golde.puzzlesafari.challenges.Challenge;
import org.golde.puzzlesafari.challenges.ChallengeElements;
import org.golde.puzzlesafari.challenges.ChallengeMineshaft;
import org.golde.puzzlesafari.challenges.ChallengeMouseMaze;
import org.golde.puzzlesafari.challenges.ChallengeParkour;
import org.golde.puzzlesafari.challenges.ChallengeSkydiving;
import org.golde.puzzlesafari.challenges.ChallengeZombieKill;
import org.golde.puzzlesafari.challenges.NotAChallengeButINeedChallengeEventsForSpawn;
import org.golde.puzzlesafari.challenges.archery.ChallengeArchery;
import org.golde.puzzlesafari.challenges.basketball.ChallengeBasketball;
import org.golde.puzzlesafari.cmds.CommandPing;
import org.golde.puzzlesafari.cmds.admin.CommandTest;
import org.golde.puzzlesafari.cmds.admin.warp.CommandDeleteWarp;
import org.golde.puzzlesafari.cmds.admin.warp.CommandListWarps;
import org.golde.puzzlesafari.cmds.admin.warp.CommandSetWarp;
import org.golde.puzzlesafari.cmds.admin.warp.CommandWarp;
import org.golde.puzzlesafari.eventhandler.EventHandlerBase;
import org.golde.puzzlesafari.eventhandler.EventHandlerChatBegin;
import org.golde.puzzlesafari.eventhandler.EventHandlerMiscWorldEvents;
import org.golde.puzzlesafari.eventhandler.EventHandlerFeatureSignManager;
import org.golde.puzzlesafari.utils.cuboid.EndCuboid;
import org.golde.puzzlesafari.utils.cuboid.EndCuboid.EventHandlerEndCuboidChecker;

public class Main extends JavaPlugin {

	private static Main instance;

	private List<EventHandlerBase> eventHandlers = new ArrayList<EventHandlerBase>();
	private List<Challenge> challenges = new ArrayList<Challenge>();

	@Override
	public void onLoad() {
		registerEventHandler(new EventHandlerMiscWorldEvents());
		registerEventHandler(new EventHandlerFeatureSignManager());
		registerEventHandler(new NotAChallengeButINeedChallengeEventsForSpawn());
		registerEventHandler(new EventHandlerChatBegin());
		registerEventHandler(new EventHandlerEndCuboidChecker());
		
		
		registerChallenge(new ChallengeMouseMaze());
		registerChallenge(new ChallengeZombieKill());
		registerChallenge(new ChallengeParkour());
		registerChallenge(new ChallengeMineshaft());
		registerChallenge(new ChallengeSkydiving());
		registerChallenge(new ChallengeArchery());
		registerChallenge(new ChallengeBasketball());
		registerChallenge(new ChallengeElements());
	}
	
	

	@Override
	public void onEnable() {
		instance = this;
		//saveDefaultConfig();
		getCommand("ping").setExecutor(new CommandPing());

		getCommand("warp").setExecutor(new CommandWarp());
		getCommand("warps").setExecutor(new CommandListWarps());
		getCommand("setwarp").setExecutor(new CommandSetWarp());
		getCommand("delwarp").setExecutor(new CommandDeleteWarp());
		getCommand("test").setExecutor(new CommandTest());

		for(EventHandlerBase fbp : eventHandlers) {
			fbp.onInternalEnable();
		}

	}

	public void callFeatureEnterFunction(Player p, String name) {

		if(name == null) {
			return;
		}

		

		for(Challenge fb : challenges) {
			if(fb.getWarpTrigger() != null && fb.getWarpTrigger().equalsIgnoreCase(name)) {
				fb.reset(p);
				fb.onEnter(p);
			}
		}

	}

	@Override
	public void onDisable() {
		for(EventHandlerBase fbp : eventHandlers) {
			fbp.onDisable();
		}
	}

	public static Main getInstance() {
		return instance;
	}

	public String color(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	private void registerChallenge(Challenge challenge) {
		challenges.add(challenge);
		registerEventHandler(challenge);
	}
	private void registerEventHandler(EventHandlerBase handler) {
		eventHandlers.add(handler);
	}

}
