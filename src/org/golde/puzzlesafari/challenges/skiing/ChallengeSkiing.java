package org.golde.puzzlesafari.challenges.skiing;

import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.golde.puzzlesafari.challenges.Challenge;

public class ChallengeSkiing extends Challenge {

	@Override
	public void onEnable() {
		//NMSUtils.registerEntity("custom_boat", Type.BOAT, CustomEntityBoat.class, false);
	}
	
	@Override
	public String getWarpTrigger() {
		return "skiing";
	}

	@Override
	public void onEnter(Player p) {
	
	}

	@Override
	public String getTitle() {
		return "SKiing";
	}
	
	@EventHandler
    public void onPlayerEnterVehicle(VehicleEnterEvent event){
        if(event.getVehicle().getType().equals(EntityType.BOAT)){
            Boat boat = (Boat) event.getVehicle();
            boat.setWorkOnLand(true);
        }
    }

}
