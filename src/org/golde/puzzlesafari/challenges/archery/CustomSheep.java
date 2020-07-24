package org.golde.puzzlesafari.challenges.archery;

import java.lang.reflect.Field;
import java.util.Set;

import net.minecraft.server.v1_12_R1.EntitySheep;
import net.minecraft.server.v1_12_R1.EnumColor;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import net.minecraft.server.v1_12_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_12_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_12_R1.World;

/**
 * Eyy another Custom Entity!
 * 
 * This is for the archery game. These sheep only have the AI of running around, and run a lot faster
 * @author Eric Golde
 *
 */
public class CustomSheep extends EntitySheep {

	@SuppressWarnings("rawtypes")
	public CustomSheep(World world) {
		super(world);

		this.setSilent(true);

		//clear goals
		Set  goalB = (Set )getPrivateField("b", PathfinderGoalSelector.class, goalSelector); goalB.clear();
		Set  goalC = (Set )getPrivateField("c", PathfinderGoalSelector.class, goalSelector); goalC.clear();
		Set  targetB = (Set )getPrivateField("b", PathfinderGoalSelector.class, targetSelector); targetB.clear();
		Set  targetC = (Set )getPrivateField("c", PathfinderGoalSelector.class, targetSelector); targetC.clear();

		//random color
		setColor(randomEnum(EnumColor.class));

		//low health
		//this.getAttributeInstance(GenericAttributes.maxHealth).setValue(0.5);
		//this.setHealth(0.5F);

		//slower speed
		this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.43030000417232513); //0.23000000417232513

		//float in water
		this.goalSelector.a(0, new PathfinderGoalFloat(this));

		//randomly stroll forever
		PathfinderGoalRandomStrollLand stroll = new PathfinderGoalRandomStrollLand(this, 1.0, 0.001f);
		stroll.setTimeBetweenMovement(1);

		this.goalSelector.a(100, stroll);
		
		//randomly look around
		this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
	}

	//something with age, idk but its to prevent a nullpointer
	@Override
	protected void M() {

	}

	//Reflection stuff for private fields woo!
	private static Object getPrivateField(String fieldName, Class<?> clazz,
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

	//Gimmi a random enum based on a enum class!
	private <T extends Enum<?>> T randomEnum(Class<T> clazz){
		int x = random.nextInt(clazz.getEnumConstants().length);
		return clazz.getEnumConstants()[x];
	}

}
