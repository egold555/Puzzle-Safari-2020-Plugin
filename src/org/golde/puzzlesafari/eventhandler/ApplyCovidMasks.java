package org.golde.puzzlesafari.eventhandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Server;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;

public class ApplyCovidMasks extends EventHandlerBase{

	private PacketAdapter adapter;

	
	//value , signature
	private static final String[] STEVE_TEXTURE = {
			"ewogICJ0aW1lc3RhbXAiIDogMTU5NDYwMDc3NjkwMSwKICAicHJvZmlsZUlkIiA6ICIwZjczMDA3NjEyNGU0NGM3YWYxMTE1NDY5YzQ5OTY3OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJPcmVfTWluZXIxMjMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWQ5MjRkNDE0YjM0ZTM4ZjBiOGM0OGIwZTliYjY0YjNlZmY2OTQxNTczNmM3ZDc2NGQxYjNlYWI3OTU4NDc3YyIKICAgIH0KICB9Cn0=",
			"RUhF4kQk3oOUxhlBlgc5vhk/TCAgun0kp/dJuVrB9lKhDzF8zCLJW8P3MZM89QDiCBT0gN12y4HiaWG0i20EjBUrQZb5ihTiRMsduIvn3K4TXkDf/Q3m4nzREJbg24pCOWUGUPjkvqC1y+ilOBOxe//LLKSzXY6RzuIKCspKTr989TzrfSUCdblYE4w0bc4bz4FVEcOJWztvI8BYBL0ka4uo8Af7mBGe5FxzMaxMiRh+weIBAPnInWE2V0lHwmu8oDtRFndcbanOftzk+RWH+rkg6fKlzDxsryZoITuWngRu+hrXSquS493RAXCUlr8kMYflQR5BkwCgOBPBscdWoohj2P3p8/MzCMvDlbnqn6UXVYVChn/MhXMb//t8WIXNLyNAOwvGIa475DAKfIWIm7j3gt0k6oh4A/CdWN7nRHRX5wWO/u5PR613d/Z0SZvxNpbwCV/mCJmy6ASyxmKc1XSdGpXu3Dwo0816Vqx2gBYb/qqMCkC6eBSjSwZpJjoRftAThAXn1nPka7n/j4cx+WKE4bwoYh233/6Fcf7AWRTXTvMG2gPd2FvDK5FcJSXSBFKEljlUhkSpOEFvEC085PhZfhrLjdOaCaFFzf2wkyY/u1jCry2FWuy4pOih4UW0lQJ81u5bB546TwBlQMWk26H6LfSzcLJsPvJ1n+VPA9Y="
	};
	
	private static final String[] ALEX_TEXTURE = {
			"ewogICJ0aW1lc3RhbXAiIDogMTU5NDYwNTg2NTg1NywKICAicHJvZmlsZUlkIiA6ICJiZWNkZGIyOGEyYzg0OWI0YTliMDkyMmE1ODA1MTQyMCIsCiAgInByb2ZpbGVOYW1lIiA6ICJTdFR2IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2NkOWVlZTQ1N2QxOTJkZTMxNjNmMTVkODRjZTA1MTE0ZjMxYzgwMzg4NDNlZGQxM2NiMzBiMTJlNWNjNDZiMjUiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
			"ToJmUd+VJDMq5M9aLWdx9XA66pCsgScQRe4N9Gitz2aRTrzptFVSCZ13J7m4spucMAciaaLwtQVEvMK7EurcA+rslEubtkpG6w8WqW5uhZxQsZJRwfiUYVB4LA/Pk+srZ9HegTl0ujRX1a9LMeCSzWfYEOZf3Y5f8hH3AZMnyG12tTx6icAlnsgXKT/9bZq0fpJm+8A3yiDaURnBClJBOC2blRtvdCfL+ZkUy3SmDZcbo6J+th7etaBPxLs/GqVVs35fNW3m4wpo7LHGXoEiDNG/0a/ms+g08vzGLC0EF0zbuw4QDSVTTuscYQsV9YUrF0/uI9gnfM2dnEx/kNSyfE33hUA4bentapY9uE9MOJi7EA8ABuyUlSekMSdi5rg5xm1ucTFMEsyq5pBbyhMB6NZNIGc/HKalA0ol/Ua/7pmCeA7typAuqcq7V/H/bWRxBTK3aIgd1sZykr4/NwTHnDnbQKMxuyl213VgzN1yUDfoL9LCTIEH0yht7oxQ1+P0czQYzii6KSrL8gMYDTW7JMPs1wrOlQk6pV2FLZeDTVulR/ugwfXz+dtWb0+g0M8dV5Iwe8U8BN8jd687MzpdxgL+ZZZF1OYgTFjyVmXpx09D9IVMLHbmnJTsf33+douDTiMgrLkH7YLJ81eR/8H72apCtjTHnEt1xtvScl6li+s="
	};
	
	@Override
	public void onEnable()
	{

		this.adapter = new PacketAdapter(getPlugin(), PacketType.Play.Server.PLAYER_INFO)
		{
			@Override
			public void onPacketSending(PacketEvent event)
			{
				PacketContainer packet = event.getPacket();
				EnumWrappers.PlayerInfoAction action = packet.getPlayerInfoAction().read(0);
				if(action != EnumWrappers.PlayerInfoAction.ADD_PLAYER)
				{
					return;
				}
				List<PlayerInfoData> data = packet.getPlayerInfoDataLists().read(0);
				for(PlayerInfoData pid : data)
				{
					WrappedGameProfile profile = pid.getProfile();
					profile.getProperties().removeAll("textures");
					if(isAlex(profile.getUUID())) {
						profile.getProperties().put("textures", new WrappedSignedProperty("textures", ALEX_TEXTURE[0], ALEX_TEXTURE[1]));
					}
					else {
						profile.getProperties().put("textures", new WrappedSignedProperty("textures", STEVE_TEXTURE[0], STEVE_TEXTURE[1]));
					}
				}
			}
		};
		ProtocolLibrary.getProtocolManager().addPacketListener(this.adapter);
	}
	
	private static boolean isAlex(UUID uuid) {
		return (uuid.hashCode() & 1) == 1;
	}

	@Override
	public void onDisable()
	{
		ProtocolLibrary.getProtocolManager().removePacketListener(this.adapter);
	}
	
}
