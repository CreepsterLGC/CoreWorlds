package main.java.me.creepsterlgc.coreworlds.commands;


import java.util.ArrayList;

import main.java.me.creepsterlgc.core.Controller;
import main.java.me.creepsterlgc.core.utils.PermissionsUtils;
import main.java.me.creepsterlgc.coreworlds.customized.CWorld;
import main.java.me.creepsterlgc.coreworlds.customized.Worlds;
import main.java.me.creepsterlgc.coreworlds.files.FileWorlds;

import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.world.DimensionType;
import org.spongepowered.api.world.DimensionTypes;
import org.spongepowered.api.world.GeneratorType;
import org.spongepowered.api.world.GeneratorTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldBuilder;
import org.spongepowered.api.world.difficulty.Difficulties;


public class CommandWorldCreate {

	public CommandWorldCreate(CommandSource sender, String[] args, Game game) {
		
		if(!PermissionsUtils.has(sender, "core.world.create")) { sender.sendMessage(Texts.builder("You do not have permissions!").color(TextColors.RED).build()); return; }
		
		if(args.length != 4) { sender.sendMessage(Texts.of(TextColors.YELLOW, "Usage: ", TextColors.GRAY, "/world create <name> <environment> <gamemode>")); return; }
		
		String name = args[1];
		String environment = args[2].toLowerCase();
		String mode = args[3].toLowerCase();
		
		if(game.getServer().getWorld(name).isPresent()) {
			sender.sendMessage(Texts.builder("World already exists!").color(TextColors.RED).build());
			return;
		}
	
		DimensionType dimension;
		GeneratorType generator;
		GameMode gamemode;
		
		
		if(environment.equalsIgnoreCase("overworld")) {
			dimension = DimensionTypes.OVERWORLD;
			generator = GeneratorTypes.OVERWORLD;
		}
		else if(environment.equalsIgnoreCase("nether")) {
			dimension = DimensionTypes.NETHER;
			generator = GeneratorTypes.NETHER;
		}
		else if(environment.equalsIgnoreCase("end")) {
			dimension = DimensionTypes.END;
			generator = GeneratorTypes.END;
		}
		else if(environment.equalsIgnoreCase("flat")) {
			dimension = DimensionTypes.OVERWORLD;
			generator = GeneratorTypes.FLAT;
		}
		else {
			sender.sendMessage(Texts.builder("<environment> has to be: overworld, nether, end or flat").color(TextColors.RED).build());
			return;
		}
		
		if(mode.equalsIgnoreCase("survival")) {
			gamemode = GameModes.SURVIVAL;
		}
		else if(mode.equalsIgnoreCase("creative")) {
			gamemode = GameModes.CREATIVE;
		}
		else if(mode.equalsIgnoreCase("adventure")) {
			gamemode = GameModes.ADVENTURE;
		}
		else if(mode.equalsIgnoreCase("spectator")) {
			gamemode = GameModes.SPECTATOR;
		}
		else {
			sender.sendMessage(Texts.builder("<gamemode> has to be: survival, creative, adventure or spectator").color(TextColors.RED).build());
			return;
		}

		sender.sendMessage(Texts.of(TextColors.GRAY, "Creating world ", TextColors.YELLOW, name, TextColors.GRAY, ".."));
		
		game.getRegistry().createBuilder(WorldBuilder.class)
		.name(name)
		.enabled(true)
		.loadsOnStartup(true)
		.keepsSpawnLoaded(true)
		.dimensionType(dimension)
		.generator(generator)
		.gameMode(gamemode)
		.build();
		
		World world = Controller.getServer().getWorld(name).get();
		
		CWorld w = new CWorld(name, false, new ArrayList<String>(), Difficulties.EASY, gamemode, true, true, true, true, true, world.getSpawnLocation(), true, false, "normal", "normal", 0, 2);
		Worlds.add(name, w);
		FileWorlds.save(w);
		
		sender.sendMessage(Texts.of(TextColors.GRAY, "World ", TextColors.YELLOW, name, TextColors.GRAY, " has been created."));
		
	}

}
