package org.golde.puzzlesafari.feature;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class FeatureBasketball extends FeatureBase {

	@Override
	public void onEnable() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				doBallPhysics();
			}
		}.runTaskTimer(getPlugin(), 1, 1);
	}
	
	@EventHandler
	public void ballFiring(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		Action a = e.getAction();
		if (a != Action.RIGHT_CLICK_AIR || e.getItem() == null) {
			return;
		}

		if(e.getItem().getType() == Material.END_BRICKS) {
			//fireSand(e.getPlayer());
			fireSlime(e.getPlayer());
		}


	}
	
	@EventHandler(priority = EventPriority.MONITOR)
    public void eventRightClick(final PlayerInteractEntityEvent e) {
        final Entity entity = e.getRightClicked();
        if (entity instanceof Slime && this.balls.contains(entity)) {
            final Slime slime = (Slime)entity;
            slime.setVelocity(slime.getVelocity().add(new Vector(0, 1, 0).add(e.getPlayer().getLocation().getDirection().normalize().multiply(0.5).setY(0))));
            slime.getWorld().playSound(slime.getLocation(), Sound.BLOCK_SLIME_HIT, 1.0f, 1.0f);
        }
    }

	private HashSet<Slime> balls = new HashSet<Slime>();
    private HashSet<UUID> ballIds = new HashSet<UUID>();
    private HashMap<UUID, Vector> velocities  = new HashMap<UUID, Vector>();
	
	private void fireSand(Player player) {
		World world = player.getWorld();
		Location loc = player.getLocation().add(0, 2, 0);
		FallingBlock falling = world.spawnFallingBlock(loc, new MaterialData(Material.END_BRICKS));
		falling.setFallDistance(1);
		//Thanks crackle <3
		falling.setVelocity(player.getLocation().getDirection().normalize().multiply(1.35));

	}
	
	@EventHandler(priority = EventPriority.MONITOR)
    public void eventLoadChunk(final ChunkLoadEvent e) {
        Entity[] entities;
        for (int length = (entities = e.getChunk().getEntities()).length, i = 0; i < length; ++i) {
            final Entity entity = entities[i];
            final UUID id = entity.getUniqueId();
            if (this.ballIds.contains(id) && entity instanceof Slime) {
                this.ballIds.remove(id);
                this.balls.add((Slime)entity);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void eventUnloadChunk(final ChunkUnloadEvent e) {
        Entity[] entities;
        for (int length = (entities = e.getChunk().getEntities()).length, i = 0; i < length; ++i) {
            final Entity entity = entities[i];
            if (entity instanceof Slime && this.balls.contains(entity)) {
                this.ballIds.add(entity.getUniqueId());
                this.balls.remove(entity);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void eventDamage(final EntityDamageEvent e) {
        if (e.getEntity() instanceof Slime && this.balls.contains(e.getEntity()) && e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            e.setCancelled(true);
        }
    }
	
	private void fireSlime(Player p) {
		final Location pos = p.getLocation();
        final Slime ball = (Slime)p.getWorld().spawnEntity(pos.add(pos.getDirection().add(new Vector(0, 1, 0))), EntityType.SLIME);
        ball.setRemoveWhenFarAway(false);
        ball.setSize(1);
        this.balls.add(ball);
        ball.setVelocity(p.getLocation().getDirection().normalize().multiply(1.35));
	}

	
	 public void doBallPhysics() {
	        for (final Slime slime : this.balls) {
	            final UUID id = slime.getUniqueId();
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
	                slime.getWorld().playSound(slime.getLocation(), Sound.ENTITY_SLIME_SQUISH, 1.0f, 1.0f);
	            }
	            slime.setVelocity(newv);
	            slime.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000, -10, true), true);
	            this.velocities.put(id, newv);
	        }
	 }

}
