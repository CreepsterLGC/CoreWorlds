package me.creepsterlgc.coreworlds.events;

import me.creepsterlgc.coreworlds.customized.CWorld;
import me.creepsterlgc.coreworlds.customized.Worlds;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.animal.Animal;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.SpawnEntityEvent;


public class EventEntitySpawn {
	
	@Listener
    public void onEntitySpawn(SpawnEntityEvent event) {
		
    	for(Entity entity : event.getEntities()) {
    		
        	CWorld w = Worlds.get(entity.getWorld().getName());
        	if(w == null) continue;
        	
	    	if(!w.getAnimalSpawning() && entity instanceof Animal || entity.getType().equals(EntityTypes.BAT)) {
	    		event.setCancelled(true);
	    		return;
	    	}
	    	if(!w.getMonsterSpawning() && entity instanceof Monster) {
	    		event.setCancelled(true);
	    		return;
	    	}
    	
    	}
		
    }

}
