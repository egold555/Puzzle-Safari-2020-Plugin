package org.golde.puzzlesafari.feature.archery;

import java.lang.reflect.Field;
import java.util.Set;

import net.minecraft.server.v1_12_R1.EntitySheep;
import net.minecraft.server.v1_12_R1.EnumColor;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import net.minecraft.server.v1_12_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_12_R1.PathfinderGoalPanic;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_12_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_12_R1.World;

public class CustomSheep extends EntitySheep {

	public CustomSheep(World world) {
		super(world);
		
		this.setSilent(true);
		
		//clear goals
		Set  goalB = (Set )getPrivateField("b", PathfinderGoalSelector.class, goalSelector); goalB.clear();
		Set  goalC = (Set )getPrivateField("c", PathfinderGoalSelector.class, goalSelector); goalC.clear();
		Set  targetB = (Set )getPrivateField("b", PathfinderGoalSelector.class, targetSelector); targetB.clear();
		Set  targetC = (Set )getPrivateField("c", PathfinderGoalSelector.class, targetSelector); targetC.clear();
		
		setColor(randomEnum(EnumColor.class));
	
		
		this.getAttributeInstance(GenericAttributes.maxHealth).setValue(0.5);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.23000000417232513);
        
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalPanic(this, 1.25));
        this.goalSelector.a(100, new PathfinderGoalRandomStrollLand(this, 100.0, 100f)); //0.001f
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
	}
		
//	//goals
//	@Override
//	protected void r() {
//		// TODO Auto-generated method stub
//		super.r();
//	}
//	
//	@Override
//	protected void initAttributes() {
//		super.initAttributes();
//		this.getAttributeInstance(GenericAttributes.maxHealth).setValue(1.0);
//        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.23000000417232513);
//	}
	
	//something with age, idk but its to prevent a nullpointer
	@Override
	protected void M() {
		
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
	
	<T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

}
