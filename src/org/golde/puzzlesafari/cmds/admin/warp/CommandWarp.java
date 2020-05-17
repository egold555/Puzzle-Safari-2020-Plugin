package org.golde.puzzlesafari.cmds.admin.warp;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.golde.puzzlesafari.cmds.base.AdminCommand;

public class CommandWarp extends AdminCommand {

	static final File file = new File("plugins/PuzzleSafari2020", "warps.yml");
    static final FileConfiguration cfg = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
	
	@Override
	public void execute2(Player p, String[] args) {
		
        if (args.length != 0) {
        	
        	final String warpName = args[0].toLowerCase();
        	
        	Location warp = getWarp(warpName);
        	
            if (warp != null) {
                
                if (args.length == 1) {
                	 p.teleport(warp);
                     p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 0.0f);
                     p.sendMessage(color("&7you have been teleported to &6" + warpName));
                }
                else if (args.length == 2) {
                	final Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        warpPlayer(target, warpName, p);
                    }
                    else {
                        p.sendMessage(color("&cPlayer not found"));
                    }
                }
                else {
                    p.sendMessage(color("&6/warp <name>"));
                }
            }
            else {
                p.sendMessage(color("&6" + warpName + " &7does not exist!"));
            }
        }
        
	}
	
	private static Location getWarp(String warpName) {
		
		if (!cfg.isSet("warps." + warpName)) {
			return null;
		}
		
		final World world = Bukkit.getWorld(cfg.getString("warps." + warpName + ".world"));
        final double x = cfg.getDouble("warps." + warpName + ".x");
        final double y = cfg.getDouble("warps." + warpName + ".y");
        final double z = cfg.getDouble("warps." + warpName + ".z");
        final float yaw = (float)cfg.getDouble("warps." + warpName + ".yaw");
        final float pitch = (float)cfg.getDouble("warps." + warpName + ".pitch");
        return new Location(world, x, y, z, yaw, pitch);
	}
	
	public static boolean warpPlayer(Player p, String warpName) {
		return warpPlayer(p, warpName, Bukkit.getConsoleSender());
	}
	
	public static boolean warpPlayer(Player p, String warpName, CommandSender sender) {
		
		Location warp = getWarp(warpName);
		if(warp == null) {
			return false;
		}
		
		p.teleport(getWarp(warpName));
        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 0.0f);
        p.sendMessage(color("&6" + p + " &7teleported you to &6" + warpName));
        
        return true;
	}

}
