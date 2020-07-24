package org.golde.puzzlesafari.challenges.basketball;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftSlime;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.golde.puzzlesafari.challenges.Challenge;
import org.golde.puzzlesafari.constants.TheGridSFX;
import org.golde.puzzlesafari.utils.NMSUtils;
import org.golde.puzzlesafari.utils.NMSUtils.Type;
import org.golde.puzzlesafari.utils.WarpManager;
import org.golde.puzzlesafari.utils.cuboid.Cuboid;
import org.golde.puzzlesafari.utils.cuboid.MobCuboid;

/**
 * Basketball challenge:
 * 		Players must throw basketballs into the hoop. 
 * 
 * 		Ball bouncing code was inspired, and derived from SethBlings BlingBall Plugin.
 * 		
 * @author Eric Golde, SethBling
 *
 */
public class ChallengeBasketball extends Challenge {

	private Cuboid goal; //hoop
	private static MobCuboid playArea; //ball spawning area / player playing area
	private static final int MAX_BALLS = 5; //How many balls should we always have in the arena?
	private static final int HOW_MANY_TO_HIT_TO_WIN = 3; //How many goals do we need to make to win
	private static final int CLEAR_TICKS = 20 * 60 * 15; //Every 15m, lets reset the balls just incase
	private static final int SPAWN_TICKS = 20; //every second, lets spawn a new ball until we reach MAX_BALLS

	private HashMap<UUID, Integer> playerScore = new HashMap<UUID, Integer>();

	@Override
	public void onEnable() {
		//Register a custom basketball entity!
		NMSUtils.registerEntity("custom_ball", Type.SLIME, CustomEntityBall.class, false);

		//make the cuboids
		goal = new Cuboid(new Location(getWorld(), -376, 12, -327), new Location(getWorld(), -375, 12, -323));
		playArea = new MobCuboid(new Location(getWorld(), -327, 3, -350), new Location(getWorld(), -377, 34, -300));

		//Start the timers!
		startTimers();


	}
	
	// /fix command to kill and reset all entities
	public static void ericFixCommand() {
		killAllBalls();
	}

	//General setup stuff for the challenges
	@Override
	public String getWarpTrigger() {
		return "basketball";
	}

	@Override
	public String getTitle() {
		return "Basket Ball";
	}
	
	//Send them a nice welcome message
	@Override
	public void onEnter(Player p) {
		sendEnterMessage(p, 
				"\"U gotta Get'cha get'cha head in the game\"", 
				"Make " + HOW_MANY_TO_HIT_TO_WIN + " baskets", MOVEMENT_WASD_RIGHT_CLICK + "throw ball"
				);
	}

	//When the plugin is disabled
	@Override
	public void onDisable() {
		killAllBalls();
	}

	private void startTimers() {
		
		//Timer to make sure the basketball count always stays at MAX_BALLS
		new BukkitRunnable() {

			@Override
			public void run() {

				//not sure why this doesn't obey mob spawning rules, but that seems to be a issue with custom sheep / net.minecraft.World
				if(playArea.getEntitiesInCuboid(Player.class).size() > 0) {
					if(playArea.getEntitiesInCuboid(Slime.class).size() < MAX_BALLS) {
						spawnBall(playArea.getRandomSpawn(6));
					}
				}


			}

		}.runTaskTimer(getPlugin(), 10, SPAWN_TICKS);

		//Check for ball physics and to check for the goals
		new BukkitRunnable() {

			@Override
			public void run() {
				doBallPhysics();
				checkForGoals();
				//goal.drawDebugParticles();
			}


		}.runTaskTimer(getPlugin(), 1, 1);

		//Timer to reset all the balls every CLEAR_TICKS
		new BukkitRunnable() {

			@Override
			public void run() {

				killAllBalls();

			}
		}.runTaskTimer(getPlugin(), 0, CLEAR_TICKS);

		//Update the actionbar every second
		new BukkitRunnable() {

			@Override
			public void run() {

				for(Entity e : playArea.getEntitiesInCuboid(Player.class)) {

					if(e instanceof Player) {
						Player p = (Player)e;

						if(!playerScore.containsKey(p.getUniqueId())) {
							playerScore.put(p.getUniqueId(), 0);
						}

						updateActionBar(p);

					}

				}

			}

		}.runTaskTimer(getPlugin(), 0, 20);
	}

	//kill all the balls
	private static void killAllBalls() {
		for(Entity z : playArea.getEntitiesInCuboid(Slime.class)) {
			((Slime)z).damage(100);
		}
	}

	//Print the actionbar message
	private void updateActionBar(Player p) {
		p.sendActionBar('&', "&eBaskets Made: &b" + playerScore.getOrDefault(p.getUniqueId(), 0) + "&f/&b" + HOW_MANY_TO_HIT_TO_WIN);
	}

	//Check for the goal
	private void checkForGoals() {
		for(Entity entity : goal.getEntitiesInCuboid(Slime.class)) {
			Slime slime = (Slime) entity;
			CraftSlime slimeCraft = (CraftSlime)slime;
			if(slimeCraft.getHandle() instanceof CustomEntityBall) {

				CustomEntityBall ball = (CustomEntityBall)slimeCraft.getHandle();

				if(ball.getGoalTarget() != null) {


					Player p = Bukkit.getPlayer(ball.getGoalTarget().getUniqueID());
					if(p != null) {

						p.playSound(p.getLocation(), TheGridSFX.BALL_GOAL, SoundCategory.AMBIENT, 1, 1);
						ball.setGoalTarget(null, TargetReason.TARGET_ATTACKED_ENTITY, false);
						UUID playerUUID = p.getUniqueId();
						int haveGottenNew = playerScore.get(playerUUID) + 1;

						if(haveGottenNew >= HOW_MANY_TO_HIT_TO_WIN) {
							//win
							reset(p);
							WarpManager.warpPlayer(p, "basketballend");
							sendFinishMessage(p, "the giant &msoccer&a basket ball");
							
							playerScore.put(playerUUID, 0);
						}
						else {
							playerScore.put(playerUUID, haveGottenNew);

						}
						updateActionBar(p);

					}




				}
			}
		}
	}

	//Right click events for debugging spawning basketballs
	@EventHandler
	public void ballFiring(PlayerInteractEvent e) {
		Action a = e.getAction();
		if (a != Action.RIGHT_CLICK_AIR || e.getItem() == null) {
			return;
		}

		if(e.getItem().getType() == Material.END_BRICKS) {
			debug_spawnBall(e.getPlayer());
		}


	}

	//------------- Essentally all SethBlings Code, modified a tiny bit to work with my code--------------------------------//
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

	private void debug_spawnBall(Player p) {
		final Location pos = p.getLocation();

		net.minecraft.server.v1_12_R1.WorldServer world = ((CraftWorld)p.getWorld()).getHandle();
		CustomEntityBall ball = new CustomEntityBall(world);
		ball.setPosition(pos.getX(), pos.getY(), pos.getZ());
		world.addEntity(ball, SpawnReason.CUSTOM);
		balls.add(ball);
		ball.setVelocity(p.getLocation().getDirection().normalize().multiply(1.35));
		ball.setGoalTarget(((CraftPlayer)p).getHandle(), TargetReason.TARGET_ATTACKED_ENTITY, false);
	}

	private void spawnBall(Location pos) {
		net.minecraft.server.v1_12_R1.WorldServer world = ((CraftWorld)pos.getWorld()).getHandle();
		CustomEntityBall ball = new CustomEntityBall(world);
		ball.setPosition(pos.getX(), pos.getY(), pos.getZ());
		world.addEntity(ball, SpawnReason.CUSTOM);
		balls.add(ball);
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

	@EventHandler
	public void entityDamageByEntity(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Slime) {
			if(e.getEntity() instanceof Player) {
				e.setCancelled(true);
			}
		}

		if(e.getDamager() instanceof Player && ((Player)e.getDamager()).getGameMode() != GameMode.CREATIVE) {
			if(e.getEntity() instanceof Slime) {
				e.setCancelled(true);
			}
		}
	}

}
