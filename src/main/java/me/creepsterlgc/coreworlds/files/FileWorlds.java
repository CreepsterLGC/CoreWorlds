package main.java.me.creepsterlgc.coreworlds.files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.difficulty.Difficulties;
import org.spongepowered.api.world.difficulty.Difficulty;

import main.java.me.creepsterlgc.core.Controller;
import main.java.me.creepsterlgc.coreworlds.customized.CWorld;
import main.java.me.creepsterlgc.coreworlds.customized.Worlds;

public class FileWorlds {
	
	public static File file = new File("config/core/modules/worlds.conf");
	public static ConfigurationLoader<?> manager = HoconConfigurationLoader.builder().setFile(file).build();
	public static ConfigurationNode worlds = manager.createEmptyNode(ConfigurationOptions.defaults());

	public static void setup() {

		try {
			
			if (!file.exists()) {
				
				file.createNewFile();
				
				for(World world : Controller.getServer().getWorlds()) {
				
					worlds.getNode("worlds", world.getName(), "private").setValue(false);
					worlds.getNode("worlds", world.getName(), "whitelist").setValue(new ArrayList<String>());
					worlds.getNode("worlds", world.getName(), "difficulty").setValue("easy");
					worlds.getNode("worlds", world.getName(), "gamemode").setValue("survival");
					worlds.getNode("worlds", world.getName(), "monsters").setValue(true);
					worlds.getNode("worlds", world.getName(), "animals").setValue(true);
					worlds.getNode("worlds", world.getName(), "pvp").setValue(true);
					worlds.getNode("worlds", world.getName(), "build").setValue(true);
					worlds.getNode("worlds", world.getName(), "interact").setValue(true);
					worlds.getNode("worlds", world.getName(), "spawn", "x").setValue(world.getSpawnLocation().getX());
					worlds.getNode("worlds", world.getName(), "spawn", "y").setValue(world.getSpawnLocation().getY());
					worlds.getNode("worlds", world.getName(), "spawn", "z").setValue(world.getSpawnLocation().getZ());
					worlds.getNode("worlds", world.getName(), "spawn", "yaw").setValue(0);
					worlds.getNode("worlds", world.getName(), "spawn", "pitch").setValue(0);
					worlds.getNode("worlds", world.getName(), "hunger").setValue(true);
					worlds.getNode("worlds", world.getName(), "invulnerability").setValue(false);
					worlds.getNode("worlds", world.getName(), "time").setValue("normal");
					worlds.getNode("worlds", world.getName(), "weather").setValue("normal");
					worlds.getNode("worlds", world.getName(), "border").setValue("0");
					worlds.getNode("worlds", world.getName(), "border-damage").setValue("2");
				
				}
				
				worlds.getNode("version").setValue(1);
				
		        manager.save(worlds);
				
			}
			
			worlds = manager.load();
			
			if(worlds.getNode("worlds").hasMapChildren()) {
				for(Entry<Object, ? extends ConfigurationNode> e : worlds.getNode("worlds").getChildrenMap().entrySet()) {
					
					String world = e.getKey().toString();
					
					if(!Controller.getServer().getWorld(world).isPresent()) continue;
					World original = Controller.getServer().getWorld(world).get();
					
					boolean priv = worlds.getNode("worlds", world, "private").getBoolean();
					
					List<String> whitelist = worlds.getNode("worlds", world, "whitelist").getList(transform);
					
					String difficulty = worlds.getNode("worlds", world, "difficulty").getString();
					Difficulty d = Difficulties.EASY;
					if(difficulty.equalsIgnoreCase("peaceful")) d = Difficulties.PEACEFUL;
					else if(difficulty.equalsIgnoreCase("easy")) d = Difficulties.EASY;
					else if(difficulty.equalsIgnoreCase("normal")) d = Difficulties.NORMAL;
					else if(difficulty.equalsIgnoreCase("hard")) d = Difficulties.HARD;
					
					String gamemode = worlds.getNode("worlds", world, "gamemode").getString();
					GameMode g = GameModes.NOT_SET;
					if(gamemode.equalsIgnoreCase("survival")) g = GameModes.SURVIVAL;
					else if(gamemode.equalsIgnoreCase("creative")) g = GameModes.CREATIVE;
					else if(gamemode.equalsIgnoreCase("adventure")) g = GameModes.ADVENTURE;
					else if(gamemode.equalsIgnoreCase("spectator")) g = GameModes.SPECTATOR;
					
					boolean monsters = worlds.getNode("worlds", world, "monsters").getBoolean();
					boolean animals = worlds.getNode("worlds", world, "animals").getBoolean();
					boolean pvp = worlds.getNode("worlds", world, "pvp").getBoolean();
					boolean build = worlds.getNode("worlds", world, "build").getBoolean();
					boolean interact = worlds.getNode("worlds", world, "interact").getBoolean();
					
					double x = worlds.getNode("worlds", world, "x").getDouble();
					double y = worlds.getNode("worlds", world, "y").getDouble();
					double z = worlds.getNode("worlds", world, "z").getDouble();
					double yaw = worlds.getNode("worlds", world, "yaw").getDouble();
					double pitch = worlds.getNode("worlds", world, "pitch").getDouble();
					
					Location<World> spawn = new Location<World>(original, x, y, z);
					
					boolean hunger = worlds.getNode("worlds", world, "hunger").getBoolean();
					boolean invulnerability = worlds.getNode("worlds", world, "invulnerability").getBoolean();
					
					String time = worlds.getNode("worlds", world, "time").getString();
					if(!time.equalsIgnoreCase("normal")
					&& !time.equalsIgnoreCase("day")
					&& !time.equalsIgnoreCase("night")
					&& !time.equalsIgnoreCase("sunrise")
					&& !time.equalsIgnoreCase("sunrise")) time = "normal";
					
					String weather = worlds.getNode("worlds", world, "weather").getString();
					if(!weather.equalsIgnoreCase("normal")
					&& !weather.equalsIgnoreCase("sun")
					&& !weather.equalsIgnoreCase("rain")) weather = "normal";
					
					double border = worlds.getNode("worlds", world, "border").getDouble();
					double borderdamage = worlds.getNode("worlds", world, "border-damage").getDouble();
					
					CWorld w = new CWorld(world, priv, whitelist, d, g, monsters, animals, pvp, build, interact, spawn, hunger, invulnerability, time, weather, border, borderdamage);
					Worlds.add(world, w);
					
				}
			}
		     
		} catch (IOException e) { e.printStackTrace(); }
		
	}
	
	public static Function<Object,String> transform = new Function<Object,String>() {
	    @Override
	    public String apply(Object input) {
	        if (input instanceof String) return (String) input;
	        else return null;
	    }
	};
	
	public static void save(CWorld world) {
		
		worlds.getNode("worlds", world.getName(), "private").setValue(world.isPrivate());
		worlds.getNode("worlds", world.getName(), "whitelist").setValue(world.getWhitelist());
		worlds.getNode("worlds", world.getName(), "difficulty").setValue(world.getDifficulty().getName());
		worlds.getNode("worlds", world.getName(), "gamemode").setValue(world.getGamemode().getName());
		worlds.getNode("worlds", world.getName(), "monsters").setValue(world.getMonsterSpawning());
		worlds.getNode("worlds", world.getName(), "animals").setValue(world.getAnimalSpawning());
		worlds.getNode("worlds", world.getName(), "pvp").setValue(world.getPVP());
		worlds.getNode("worlds", world.getName(), "build").setValue(world.getBuild());
		worlds.getNode("worlds", world.getName(), "interact").setValue(world.getInteract());
		worlds.getNode("worlds", world.getName(), "spawn", "x").setValue(world.getSpawn().getX());
		worlds.getNode("worlds", world.getName(), "spawn", "y").setValue(world.getSpawn().getY());
		worlds.getNode("worlds", world.getName(), "spawn", "z").setValue(world.getSpawn().getZ());
		worlds.getNode("worlds", world.getName(), "spawn", "yaw").setValue(0);
		worlds.getNode("worlds", world.getName(), "spawn", "pitch").setValue(0);
		worlds.getNode("worlds", world.getName(), "hunger").setValue(world.getHunger());
		worlds.getNode("worlds", world.getName(), "invulnerability").setValue(world.getInvulnerability());
		worlds.getNode("worlds", world.getName(), "time").setValue(world.getTime());
		worlds.getNode("worlds", world.getName(), "weather").setValue(world.getWeather());
		worlds.getNode("worlds", world.getName(), "border").setValue(world.getBorder());
		worlds.getNode("worlds", world.getName(), "border-damage").setValue(world.getBorderDamage());
		
        try { manager.save(worlds); worlds = manager.load(); }
        catch (IOException e) { e.printStackTrace(); }
        
	}
	
}
