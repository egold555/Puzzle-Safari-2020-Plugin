package org.golde.puzzlesafari.feature.balltoss;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.EntitySlime;
import net.minecraft.server.v1_12_R1.EnumDifficulty;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_12_R1.SoundEffect;
import net.minecraft.server.v1_12_R1.World;

public class CustomEntityBall extends EntitySlime {

	//bw is a private variable, making it accessable
	private boolean bw;

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

	//bw is a private variable, making it accessable
	@Override
	public void a(final NBTTagCompound nbttagcompound) {
		super.a(nbttagcompound);
		this.bw = nbttagcompound.getBoolean("wasOnGround");
	}
//
//	//removal of particle effects
//	@Override
//	public void B_() {
//		if (!this.world.isClientSide && this.world.getDifficulty() == EnumDifficulty.PEACEFUL && this.getSize() > 0) {
//			this.dead = true;
//		}
//		this.b += (this.a - this.b) * 0.5f;
//		this.c = this.b;
//		super.B_();
//		if (this.onGround && !this.bw) {
//			this.a(this.dj(), this.cq(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f) / 0.8f);
//			this.a = -0.5f;
//		}
//		else if (!this.onGround && this.bw) {
//			this.a = 1.0f;
//		}
//		this.bw = this.onGround;
//		this.dg();
//	}
//	
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

	static Object getPrivateField(String fieldName, Class clazz,
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
