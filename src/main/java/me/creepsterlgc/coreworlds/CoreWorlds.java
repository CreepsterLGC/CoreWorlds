package main.java.me.creepsterlgc.coreworlds;

import java.io.File;
import java.util.logging.Logger;

import main.java.me.creepsterlgc.core.files.FileCommands;
import main.java.me.creepsterlgc.coreworlds.commands.CommandWorld;
import main.java.me.creepsterlgc.coreworlds.events.EventEntitySpawn;
import main.java.me.creepsterlgc.coreworlds.events.EventPlayerBlockBreak;
import main.java.me.creepsterlgc.coreworlds.events.EventPlayerBlockPlace;
import main.java.me.creepsterlgc.coreworlds.events.EventPlayerInteractBlock;
import main.java.me.creepsterlgc.coreworlds.files.FileWorlds;

import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import com.google.inject.Inject;

@Plugin(id = "CoreWorlds", name = "Worlds Module for Core", dependencies = "required-after:Core")

public class CoreWorlds {

	@Inject
	private Game game;
	
	@Inject
	Logger logger;
	
	public static CoreWorlds core;
	
	public static CoreWorlds getInstance() { return core; }
	public Game getGame() { return game; }
	
    @Listener
    public void onEnable(GameStartingServerEvent event) {
    	
    	File folder = new File("config/core/modules");
    	if(!folder.exists()) folder.mkdir();
    	
    	FileWorlds.setup();
    	
    	game.getEventManager().registerListeners(this, new EventEntitySpawn());
    	game.getEventManager().registerListeners(this, new EventPlayerBlockPlace());
    	game.getEventManager().registerListeners(this, new EventPlayerBlockBreak());
    	game.getEventManager().registerListeners(this, new EventPlayerInteractBlock());
    	
    	if(FileCommands.WORLD()) game.getCommandDispatcher().register(this, new CommandWorld(game), "world");
    	
    }
    
}