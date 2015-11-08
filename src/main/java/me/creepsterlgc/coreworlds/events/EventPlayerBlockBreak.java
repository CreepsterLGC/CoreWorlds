package main.java.me.creepsterlgc.coreworlds.events;

import java.util.Optional;

import main.java.me.creepsterlgc.core.utils.PermissionsUtils;
import main.java.me.creepsterlgc.coreworlds.customized.CWorld;
import main.java.me.creepsterlgc.coreworlds.customized.Worlds;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;


public class EventPlayerBlockBreak {

    @Listener
    public void onPlayerBlockBreak(ChangeBlockEvent.Break event) {
    	
    	Optional<Player> optional = event.getCause().first(Player.class);
    	if(!optional.isPresent()) return;

    	Player player = optional.get();
    	CWorld w = Worlds.get(event.getTargetWorld().getName());
    	
    	if(w == null) return;
	    if(!w.getBuild() && !PermissionsUtils.has(player, "core.world." + w.getName() + ".bypass.build")) {
		    event.setCancelled(true);
	    }
	    
    }
	
}
