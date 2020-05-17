package org.golde.puzzlesafari;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.golde.puzzlesafari.cmds.CommandPing;
import org.golde.puzzlesafari.cmds.CommandTest;
import org.golde.puzzlesafari.cmds.admin.warp.CommandDeleteWarp;
import org.golde.puzzlesafari.cmds.admin.warp.CommandListWarps;
import org.golde.puzzlesafari.cmds.admin.warp.CommandSetWarp;
import org.golde.puzzlesafari.cmds.admin.warp.CommandWarp;
import org.golde.puzzlesafari.feature.FeatureMiscWorldEvents;
import org.golde.puzzlesafari.feature.FeatureSlimeJump;
import org.golde.puzzlesafari.feature.zombie.FeatureZombieKill;
import org.golde.puzzlesafari.feature.FeatureBase;

public class Main extends JavaPlugin {

	private static Main instance;
	
	private List<FeatureBase> features = new ArrayList<FeatureBase>();
	
	@Override
	public void onLoad() {
		features.add(new FeatureMiscWorldEvents());
		features.add(new FeatureSlimeJump());
		features.add(new FeatureZombieKill());
	}

	@Override
	public void onEnable() {
		instance = this;
		//saveDefaultConfig();
		getCommand("test").setExecutor(new CommandTest());
		getCommand("ping").setExecutor(new CommandPing());
		
		getCommand("warp").setExecutor(new CommandWarp());
		getCommand("warps").setExecutor(new CommandListWarps());
		getCommand("setwarp").setExecutor(new CommandSetWarp());
		getCommand("delwarp").setExecutor(new CommandDeleteWarp());
		
		for(FeatureBase fbp : features) {
			fbp.onEnable();
		}
		
	}
	
	@Override
	public void onDisable() {
		for(FeatureBase fbp : features) {
			fbp.onDisable();
		}
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public String color(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
}
