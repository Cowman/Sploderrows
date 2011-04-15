package me.cowman94.sploderrows;

import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftArrow;
import org.bukkit.craftbukkit.entity.CraftEntity;
//import org.bukkit.entity.Entity;

import me.cowman94.sploderrows.*;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityTNTPrimed;
import net.minecraft.server.Explosion;
import net.minecraft.server.World;
//import net.minecraft.server.World;

public class SploderrowsSplode {
	private final Sploderrows plugin;
	
	
	public SploderrowsSplode(Sploderrows instance){
		plugin = instance;
	}
	
	public void splodeArrow(SploderrowsArrows a, Float force){
		World world = ((CraftWorld)a.arrow.getWorld()).getHandle(); 
		Location loc = a.arrow.getLocation();
		CraftEntity spl = ((CraftArrow)a.arrow);
		world.a(null, loc.getX(), loc.getY(), loc.getZ(), force);
	}
	
	public void smokeArrow(SploderrowsArrows and, double x, double y, double z){
		World world = ((CraftWorld)and.arrow.getWorld()).getHandle(); 
		Location loc = and.arrow.getLocation();
		for (int j=0;j<8;j++){
			world.a("snowballpoof", x, y + 0.5D, z, 0.0D, 0.0D, 0.0D);
			world.a(x, y + 0.5D, z, "random.fuse", 1.0F, 0.5F);
		}
	}
}
