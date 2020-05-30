package org.golde.puzzlesafari.feature.balltoss;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.golde.puzzlesafari.constants.TheGridSFX;
import org.golde.puzzlesafari.feature.FeatureBase;
import org.golde.puzzlesafari.utils.NMSUtils;
import org.golde.puzzlesafari.utils.NMSUtils.Type;
import org.golde.puzzlesafari.utils.cuboid.Cuboid;

public class FeatureBasketball extends FeatureBase {

	Cuboid goal;

	@Override
	public void onEnable() {
		NMSUtils.registerEntity("custom_ball", Type.SLIME, CustomEntityBall.class, false);

		goal = new Cuboid(new Location(getWorld(), -374, 15, -320), new Location(getWorld(), -376, 15, -330));

		new BukkitRunnable() {

			@Override
			public void run() {
				doBallPhysics();
				checkForGoals();
			}


		}.runTaskTimer(getPlugin(), 1, 1);
	}

	private void checkForGoals() {
		for(Entity entity : goal.getEntitiesInCuboid()) {
			net.minecraft.server.v1_12_R1.Entity netEntity = ((CraftEntity) entity).getHandle();
			if(netEntity instanceof CustomEntityBall) {

				CustomEntityBall ball = (CustomEntityBall)netEntity;

				if(ball.getGoalTarget() != null) {

					try {
						Player p = Bukkit.getPlayer(ball.getGoalTarget().getUniqueID());
						if(p != null) {
							p.playSound(p.getLocation(), TheGridSFX.BALL_GOAL, SoundCategory.AMBIENT, 1, 1);
							ball.setGoalTarget(null, TargetReason.TARGET_ATTACKED_ENTITY, false);
						}
					}
					catch(Exception e) {
						//ignored
					}


				}
			}
		}
	}

	@EventHandler
	public void ballFiring(PlayerInteractEvent e) {
		Action a = e.getAction();
		if (a != Action.RIGHT_CLICK_AIR || e.getItem() == null) {
			return;
		}

		if(e.getItem().getType() == Material.END_BRICKS) {
			fireSlime(e.getPlayer());
		}


	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void eventRightClick(final PlayerInteractEntityEvent e) {
		final Entity entity = e.getRightClicked();
		net.minecraft.server.v1_12_R1.Entity netEntity = ((CraftEntity) entity).getHandle();
		if (netEntity instanceof CustomEntityBall && this.balls.contains(netEntity)) {
			final CustomEntityBall slime = (CustomEntityBall)netEntity;
			slime.setVelocity(slime.getVelocity().add(new Vector(0, 1, 0).add(e.getPlayer().getLocation().getDirection().normalize().multiply(0.5).setY(0))));
			slime.getWorldBukkit().playSound(slime.getLocation(), TheGridSFX.BALL_BOUNCE, 1.0f, 1.0f);
			slime.setGoalTarget(((CraftPlayer)e.getPlayer()).getHandle(), TargetReason.TARGET_ATTACKED_ENTITY, false);
		}
	}

	private HashSet<CustomEntityBall> balls = new HashSet<CustomEntityBall>();
	private HashSet<UUID> ballIds = new HashSet<UUID>();
	private HashMap<UUID, Vector> velocities  = new HashMap<UUID, Vector>();

	@EventHandler(priority = EventPriority.MONITOR)
	public void eventLoadChunk(final ChunkLoadEvent e) {
		Entity[] entities;
		for (int length = (entities = e.getChunk().getEntities()).length, i = 0; i < length; ++i) {
			final Entity entity = entities[i];
			final net.minecraft.server.v1_12_R1.Entity netEntity = ((CraftEntity) entity).getHandle();
			final UUID id = entity.getUniqueId();
			if (this.ballIds.contains(id) && netEntity instanceof CustomEntityBall) {
				this.ballIds.remove(id);
				this.balls.add((CustomEntityBall) netEntity);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void eventUnloadChunk(final ChunkUnloadEvent e) {
		Entity[] entities;
		for (int length = (entities = e.getChunk().getEntities()).length, i = 0; i < length; ++i) {
			final Entity entity = entities[i];
			final net.minecraft.server.v1_12_R1.Entity netEntity = ((CraftEntity) entity).getHandle();
			if (netEntity instanceof CustomEntityBall && this.balls.contains(netEntity)) {
				this.ballIds.add(entity.getUniqueId());
				this.balls.remove(netEntity);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void eventDamage(final EntityDamageEvent e) {
		final net.minecraft.server.v1_12_R1.Entity netEntity = ((CraftEntity) e.getEntity()).getHandle();
		if (netEntity instanceof CustomEntityBall && this.balls.contains(netEntity) && e.getCause() == EntityDamageEvent.DamageCause.FALL) {
			e.setCancelled(true);
		}
	}

	private void fireSlime(Player p) {
		final Location pos = p.getLocation();

		net.minecraft.server.v1_12_R1.WorldServer world = ((CraftWorld)p.getWorld()).getHandle();
		CustomEntityBall ball = new CustomEntityBall(world);
		ball.setPosition(pos.getX(), pos.getY(), pos.getZ());
		world.addEntity(ball, SpawnReason.CUSTOM);
		balls.add(ball);
		ball.setVelocity(p.getLocation().getDirection().normalize().multiply(1.35));
		ball.setGoalTarget(((CraftPlayer)p).getHandle(), TargetReason.TARGET_ATTACKED_ENTITY, false);
	}


	public void doBallPhysics() {
		for (final CustomEntityBall slime : this.balls) {
			final UUID id = slime.getUniqueID();
			Vector velocity = slime.getVelocity();
			if (this.velocities.containsKey(id)) {
				velocity = this.velocities.get(id);
			}
			if (slime.isDead()) {
				continue;
			}
			Boolean bounceSound = false;
			final Vector newv = slime.getVelocity();
			if (newv.getX() == 0.0) {
				newv.setX(-velocity.getX() * 0.8);
				if (Math.abs(velocity.getX()) > 0.3) {
					bounceSound = true;
				}
			}
			else if (Math.abs(velocity.getX() - newv.getX()) < 0.1) {
				newv.setX(velocity.getX() * 0.98);
			}
			if (newv.getY() == -0.0784000015258789 && velocity.getY() < -0.1) {
				newv.setY(-velocity.getY() * 0.8);
				if (Math.abs(velocity.getY()) > 0.3) {
					bounceSound = true;
				}
			}
			if (newv.getZ() == 0.0) {
				newv.setZ(-velocity.getZ() * 0.8);
				if (Math.abs(velocity.getZ()) > 0.3) {
					bounceSound = true;
				}
			}
			else if (Math.abs(velocity.getZ() - newv.getZ()) < 0.1) {
				newv.setZ(velocity.getZ() * 0.98);
			}
			if (bounceSound) {
				slime.getWorldBukkit().playSound(slime.getLocation(), TheGridSFX.BALL_BOUNCE, 1.0f, 1.0f);
			}
			slime.setVelocity(newv);
			this.velocities.put(id, newv);
		}
	}

}
