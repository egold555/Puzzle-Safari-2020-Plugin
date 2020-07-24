package org.golde.puzzlesafari.challenges.basketball;

import java.lang.reflect.Field;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.EntitySlime;
import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_12_R1.SoundEffect;
import net.minecraft.server.v1_12_R1.World;

/**
 * Ok so there is most likely a much better way of doing this, but I could not think of any other way
 * 
 * This class is a custom entity I register into the game to be the basketball
 * 
 * Its essentally a slime with no AI, silent, no loot.
 * 
 * Plan was to implement the physics into this class, and I also did not know if I was oging to use slimes
 * for another challenge.
 * 
 * @author Eric Golde
 *
 */
public class CustomEntityBall extends EntitySlime {

	@SuppressWarnings("rawtypes")
	public CustomEntityBall(World world) {
		super(world);
		setSilent(true);
		setSize(2, true);

		//no ai
		Set  goalB = (Set )getPrivateField("b", PathfinderGoalSelector.class, goalSelector); goalB.clear();
		Set  goalC = (Set )getPrivateField("c", PathfinderGoalSelector.class, goalSelector); goalC.clear();
		Set  targetB = (Set )getPrivateField("b", PathfinderGoalSelector.class, targetSelector); targetB.clear();
		Set  targetC = (Set )getPrivateField("c", PathfinderGoalSelector.class, targetSelector); targetC.clear();
	}
	
	@Override
    protected SoundEffect d(final DamageSource damagesource) {
        return null;
    }
    
    @Override
    protected SoundEffect cf() {
        return null;
    }
    
    protected SoundEffect dj() {
        return null;
    }
    
    @Override
    protected Item getLoot() {
        return null;
    }
    
    protected SoundEffect dk() {
        return null;
    }

	//not split
	@Override
	public void die() {
		//entity.class
		this.dead = true;
	}

	static Object getPrivateField(String fieldName, Class<?> clazz,
			Object object) {
		Field field;
		Object o = null;
		try {
			field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			o = field.get(object);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return o;
	}



	//////////////////// bukkit /////////////////////////////

	public void setVelocity(final Vector velocity) {
		this.motX = velocity.getX();
		this.motY = velocity.getY();
		this.motZ = velocity.getZ();
		this.velocityChanged = true;
	}

	public Location getLocation() {
		return new Location(getWorldBukkit(), this.locX, this.locY, this.locZ, this.getBukkitYaw(), this.pitch);
	}

	public org.bukkit.World getWorldBukkit() {
		return this.world.getWorld();
	}

	public Vector getVelocity() {
		return new Vector(this.motX, this.motY, this.motZ);
	}

	public boolean isDead() {
		return !this.isAlive();
	}


}
