package org.golde.puzzlesafari.cmds.admin;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.golde.puzzlesafari.challenges.basketball.CustomEntityBall;
import org.golde.puzzlesafari.challenges.skiing.CustomEntityBoat;
import org.golde.puzzlesafari.cmds.base.AdminCommand;

import net.minecraft.server.v1_12_R1.Entity;

public class CommandTest extends AdminCommand {

	@Override
	public void execute2(Player sender, String[] args) {
		
		World world = sender.getWorld();
		Location loc = sender.getLocation();
		net.minecraft.server.v1_12_R1.WorldServer nmsWorld = ((CraftWorld)world).getHandle();
		
		Entity e = new CustomEntityBoat(nmsWorld);
		e.setPosition(loc.getX(), loc.getY(), loc.getZ());
		nmsWorld.addEntity(e);
		
		
		sender.sendMessage("Spawned.");
		
	}

}
