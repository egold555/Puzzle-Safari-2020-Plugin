package org.golde.puzzlesafari.feature;

import java.lang.reflect.Field;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class FeatureMouseMaze extends FeatureBase {
	
	@Override
	public void onEnable() {
		new BukkitRunnable() {

			@Override
			public void run() {
//				for(Player p : Bukkit.getOnlinePlayers()) {
//					if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SLIME_BLOCK && p.getLocation().subtract(0, 1, 0).getBlock().getRelative(BlockFace.DOWN).getType() == Material.STAINED_GLASS) {
//						p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20*2, 13, true), true);
//					}
//				}
			}

		}.runTaskTimer(getPlugin(), 0, 20);
	}

	@Override
	public String getWarpTrigger() {
		return "mouse";
	}

	@Override
	public void onEnter(Player p) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000000, 3, true));

		ItemStack helmet = getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzExZGMzNGM2YzMzZWQ1YzJlOTc1ZjU4YWJiNDVkNWRiZWEzZDUxMzM1YjllZmRmZDIzZGU0ZjVkNWJhOGQ2MCJ9fX0=");
		ItemMeta helmetMeta = helmet.getItemMeta();
		helmetMeta.setDisplayName(ChatColor.GRAY + "Mouse Disguise");
		helmet.setItemMeta(helmetMeta);
		
		Color color = Color.SILVER;
		
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplate.getItemMeta();
		chestplateMeta.setColor(color);
		chestplateMeta.setDisplayName(ChatColor.GRAY + "Mouse Disguise");
		chestplate.setItemMeta(chestplateMeta);
		
		ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
		LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggings.getItemMeta();
		leggingsMeta.setColor(color);
		leggingsMeta.setDisplayName(ChatColor.GRAY + "Mouse Disguise");
		leggings.setItemMeta(leggingsMeta);
		
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();
		bootsMeta.setColor(color);
		bootsMeta.setDisplayName(ChatColor.GRAY + "Mouse Disguise");
		boots.setItemMeta(bootsMeta);
		
		
		PlayerInventory pinv = p.getInventory();
		
		pinv.setBoots(boots);
		pinv.setLeggings(leggings);
		pinv.setChestplate(chestplate);
		pinv.setHelmet(helmet);
		
		p.updateInventory();
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SLIME_BLOCK && p.getLocation().subtract(0, 1, 0).getBlock().getRelative(BlockFace.DOWN).getType() == Material.STAINED_GLASS) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20*2, 10, true), true);
		}
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			ItemStack helmet = p.getInventory().getHelmet();
			if(helmet != null && helmet.getItemMeta() != null && helmet.getItemMeta().getDisplayName() != null && helmet.getItemMeta().getDisplayName().contains("Mouse")) {
				e.setCancelled(true);
			}
		}
	}

	private ItemStack getCustomTextureHead(String value) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", value));
        Field profileField = null;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        head.setItemMeta(meta);
        return head;
    }

}
