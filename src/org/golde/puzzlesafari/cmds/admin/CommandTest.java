package org.golde.puzzlesafari.cmds.admin;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.golde.puzzlesafari.cmds.base.AdminCommand;
import org.golde.puzzlesafari.feature.archery.CustomSheep;

public class CommandTest extends AdminCommand {

	@Override
	public void execute2(Player sender, String[] args) {
		
		World world = sender.getWorld();
		Location loc = sender.getLocation();
		
		net.minecraft.server.v1_12_R1.WorldServer nmsWorld = ((CraftWorld)world).getHandle();
		CustomSheep sheep = new CustomSheep(nmsWorld);
		sheep.setPosition(loc.getX(), loc.getY(), loc.getZ());
		nmsWorld.addEntity(sheep);
		sender.sendMessage("Spawned.");
		
	}

}