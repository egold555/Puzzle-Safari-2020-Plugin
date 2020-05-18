package org.golde.puzzlesafari.feature.parkour;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

import net.minecraft.server.v1_12_R1.EntityGuardian;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_12_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_12_R1.World;

//https://www.spigotmc.org/threads/tutorial-creating-custom-entities-with-pathfindergoals.18519/
public class EntityCustomGuardian extends EntityGuardian {

	public EntityCustomGuardian(World world)
    {
        super(world);

        this.goalSelector = new PathfinderGoalSelector(getWorld().methodProfiler);
        this.targetSelector = new PathfinderGoalSelector(getWorld().methodProfiler);
        
        PathfinderGoalRandomStroll stroll = new PathfinderGoalRandomStroll(this, 1.0, 100);
        stroll.setTimeBetweenMovement(5);
        stroll.
        //stroll.i();
        
        this.goalRandomStroll = stroll;
        this.goalSelector.a(100, goalRandomStroll);
        setSilent(true);
    }
	
	public EntityCustomGuardian(org.bukkit.World world)
    {
        super(((CraftWorld)world).getHandle());
    }
	
}
