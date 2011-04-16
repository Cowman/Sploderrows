package me.cowman94.sploderrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.server.World;
import org.bukkit.entity.Creeper;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.config.Configuration;
import me.cowman94.sploderrows.*;


public class SploderrowsEntityListener extends EntityListener {
    private final Sploderrows plugin;
    public List<Arrow> sarrows;
    public List<SploderrowsArrows> arrows;
    public List<SploderrowsArrows> remarrows;
    public SploderrowsEntityListener(Sploderrows instance) {
        plugin = instance;

    }
    //Insert Player related code here
    @Override
    public void onEntityDamage(EntityDamageEvent event)
    {
        super.onEntityDamage(event);
        
        //final HashMap<Arrow, Boolean> rows = plugin.arrowControl.rows;
        sarrows = plugin.arrowControl.sarrows;   
        arrows = plugin.arrowControl.arrows; 
        remarrows = plugin.arrowControl.remarrows; 
        if (event instanceof EntityDamageByProjectileEvent)
        {
         EntityDamageByProjectileEvent edee = (EntityDamageByProjectileEvent) event;
         if(edee.getProjectile() instanceof Arrow) {
        	 
        	 Arrow a = (Arrow) edee.getProjectile();
         
         if(edee.getDamager() instanceof Player) {
         Player p = (Player) edee.getDamager();
         //intDamageMultiplier = plugin.config.getInt("Sploderrows.Users." + p.getName() + ".damageMultiplier", plugin.config.getInt("Sploderrows.Default.damageMultiplier", 1));
         //useFireArrows = plugin.config.getBoolean("Sploderrows.Users." + p.getName() + ".fireArrows.entity", plugin.config.getBoolean("Sploderrows.Default.fireArrows.entity", false));
         event.setDamage(event.getDamage()*1);
         if(sarrows.contains(a)) {
         LivingEntity e = (LivingEntity) edee.getEntity();
         int ticks = (e.getHealth()*2)*10;
         e.setFireTicks(ticks);
         
         }
         if (!arrows.get(sarrows.indexOf(a)).setBlockOnFire){
        	 plugin.sploder.splodeArrow(arrows.get(sarrows.indexOf(a)),2F);
			 //remarrows.add(arrows.get(sarrows.indexOf(a)));
         }
         plugin.arrowControl.remarrows.add(plugin.arrowControl.arrows.get(plugin.arrowControl.sarrows.indexOf(a)));
         }
         
         }
         
         
        }
        
    }
    
    public void onEntityDeath(EntityDeathEvent event){
    	if (event.getEntity() instanceof Arrow){
    	Entity a = event.getEntity();
    	if (plugin.arrowControl.sarrows.contains(a)) {  	
    	plugin.arrowControl.remarrows.add(plugin.arrowControl.arrows.get(plugin.arrowControl.sarrows.indexOf(a)));}
    	}
    }
    
    public void onEntityCombust(EntityCombustEvent event){
		
		if (event.getEntity() instanceof Creeper){
			Location loc = event.getEntity().getLocation();
		
		World world = ((CraftWorld)event.getEntity().getWorld()).getHandle(); 
		world.a(null, loc.getX(), loc.getY(), loc.getZ(), 3F);
		}
		
    }
}

