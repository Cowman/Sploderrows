package me.cowman94.sploderrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import me.cowman94.sploderrows.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;
import org.bukkit.craftbukkit.entity.CraftArrow;

public class SploderrowsArrowControl {
private final Sploderrows plugin;
private long lastTick = 0;
private static final Logger log = Logger.getLogger("Minecraft");
public List<SploderrowsArrows> arrows;
public List<SploderrowsArrows> remarrows;
public List<Arrow> sarrows;
public HashMap<Integer, Boolean> rows = new HashMap<Integer, Boolean>();
    public SploderrowsArrowControl(Sploderrows instance) {
        plugin = instance;
        arrows = new ArrayList<SploderrowsArrows>();
        sarrows = new ArrayList<Arrow>();
        remarrows = new ArrayList<SploderrowsArrows>();
    }
    public void addArrow(SploderrowsArrows sba , Boolean splode) {
     arrows.add(sba);
     sarrows.add(sba.arrow);
     this.rows.put(sba.arrow.getEntityId(), splode);
     System.out.println(sarrows.contains(sba.arrow));
     System.out.println(sarrows.isEmpty());
     System.out.println(arrows.isEmpty());
     System.out.println(rows.isEmpty());
     System.out.println(rows.containsKey(sba.arrow.getEntityId()));
     System.out.println(rows);
    }
    public void removeByID(int eID) {
     if(!arrows.isEmpty()) {
    	 Iterator itr = arrows.iterator();
    	 while(itr.hasNext()) {
    		 	SploderrowsArrows a = (SploderrowsArrows) itr.next();
    		 	if(a.arrow.getEntityId() == eID) {
    		 		arrows.remove(a);
    		 		sarrows.remove(a.arrow);
    		 		rows.remove(eID);
    		 		System.out.println(rows.containsKey(eID));
    		 		
    		 		return;
    		 	}
    	 }
     }
    }
    
    public void checkArrows(boolean tactical, Player play) {
    	
    	if(!arrows.isEmpty() && plugin.getServer().getWorld("world").getTime() != lastTick) {
    		lastTick = plugin.getServer().getWorld("world").getTime();
    		for (int i=0;i < arrows.size();i++){
    			SploderrowsArrows a = arrows.get(i);
    			if(!a.waitTillNextCall) {
    				
    				Location curLoc = a.arrow.getLocation();
    				plugin.sploder.smokeArrow(a, curLoc.getX(), curLoc.getY(), curLoc.getZ());
    				if (a.arrow.isDead()){remarrows.add(a);}
    				//plugin.sploder.smokeArrow(a, curLoc.getX(), curLoc.getY(), curLoc.getZ());
    				if(((curLoc.equals(a.lastPostion) && !a.lastPostion.equals(a.owner.getLocation())&& !a.arrow.isDead()) || (tactical && play == a.owner && plugin.getBool(a.owner.getWorld(), "midAir")))  && rows.get(a.arrow.getEntityId()) ) {
    					boolean blnSetFire = false;
    					
    					Block b = curLoc.getBlock();
    					World world = b.getWorld();
    					//int x=0,y =0,z = 0;
    					for(int x = -plugin.getInt(a.owner.getWorld(), "fireRadius");x < plugin.getInt(a.owner.getWorld(), "fireRadius")+1; x++) {
    						for(int y = -plugin.getInt(a.owner.getWorld(), "fireRadius");y < plugin.getInt(a.owner.getWorld(), "fireRadius")+1; y++) {
    							for(int z = -plugin.getInt(a.owner.getWorld(), "fireRadius");z < plugin.getInt(a.owner.getWorld(), "fireRadius")+1; z++) {
    								if(world.getBlockAt(b.getX() + x, b.getY() + y, b.getZ() + z).getType() == Material.AIR) {
    									blnSetFire = true;
    									world.getBlockAt(b.getX() + x, b.getY() + y, b.getZ() + z).setType(Material.FIRE);
    								}
    							}
    						}
    					}
    					remarrows.add(a);
    					if(blnSetFire) {
    						
    						
    					}
    					
    				
    				}
    				if(((curLoc.equals(a.lastPostion) && !a.lastPostion.equals(a.owner.getLocation())&& !a.arrow.isDead()) || (tactical && a.tactical && plugin.getBool(a.owner.getWorld(), "midAir")))  && !rows.get(a.arrow.getEntityId()) ) {
						if(a.tactical == true && tactical && a.owner == play){
    					plugin.sploder.splodeArrow(a,plugin.getFloat(a.owner.getWorld(), "tacticalForce"));
		    			remarrows.add(a);
						}
						if(a.tactical == false){
	    					plugin.sploder.splodeArrow(a,plugin.getFloat(a.owner.getWorld(), "splodeForce"));
			    			remarrows.add(a);
							}
					}
    				if(!curLoc.equals(a.lastPostion)) {
    					a.lastPostion = curLoc;
    					continue;
    				}
    			} else {
    				a.waitTillNextCall = false;
    			}
    		}
    		
    		for (int f=0; f < remarrows.size(); f++){
    			arrows.remove(remarrows.get(f));
    			sarrows.remove(remarrows.get(f).arrow);
    			rows.remove(remarrows.get(f).arrow.getEntityId());
    			remarrows.get(f).arrow.remove();
    		}
    		remarrows.clear();
    	}
    }
}