package me.creepsterlgc.coreworlds.customized;

import java.util.HashMap;

public class Worlds {
	
	private static HashMap<String, CWorld> worlds = new HashMap<String, CWorld>();
	public static void add(String name, CWorld world) { if(!worlds.containsKey(name)) worlds.put(name, world); }
	public static void remove(String name) { if(worlds.containsKey(name)) worlds.remove(name); }
	public static CWorld get(String name) { return worlds.containsKey(name) ? worlds.get(name) : null; }
	public static HashMap<String, CWorld> all() { return worlds; }
	
}
