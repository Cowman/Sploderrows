package me.cowman94.sploderrows;

//import net.minecraft.server.Entity;
import net.minecraft.server.EntityTNTPrimed;
import net.minecraft.server.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
//import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Entity;
import com.earth2me.essentials.TargetBlock;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;


public class SploderrowsPlayerListener extends PlayerListener {
    private final Sploderrows plugin;

    public SploderrowsPlayerListener(Sploderrows instance) {
        plugin = instance;
    }

    public void onPlayerJoin(PlayerJoinEvent event) {
        //System.out.println(event.getPlayer().getName() + " joined the server! :D" + event.getPlayer().getWorld().getName());
    }

    public void onPlayerQuit(PlayerQuitEvent event){
        //System.out.println(event.getPlayer().getName() + " left the server! :'(");
    }
    
    public void onPlayerInteract(PlayerInteractEvent event){
    	Player player = event.getPlayer();
    	World world = ((CraftWorld)player.getWorld()).getHandle();
    	Location location = player.getLocation();
    	Vector vector = location.toVector();
    	Block block = event.getClickedBlock();
    	Action action = event.getAction();
    	BlockFace blockFace = event.getBlockFace();
    	ItemStack item = event.getItem();
    	Material splodeBow = Material.getMaterial(plugin.getInt(player.getWorld().getName() + ".splodebow"));
    	//System.out.println(plugin.getInt(player.getWorld().getName() + ".splodebow"));
    	Material fireBow = Material.getMaterial(plugin.getInt(player, "firebow"));
    	if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK){
    		try{
    		if (item.getType() == splodeBow){
    			if (plugin.deductArrows(player, plugin.getInt(player.getWorld(), "splodecost"))){
    				new SploderrowsArrows(player, player.shootArrow(), false, plugin);
    				//player.sendMessage(ChatColor.RED + "Pwang!");
    				player.updateInventory();
    			}
    		}
			if (item.getType() == fireBow){
				if (plugin.deductArrows(player, plugin.getInt(player.getWorld(), "firecost"))){
					new SploderrowsArrows(player, player.shootArrow(), true, plugin);
					//player.sendMessage(ChatColor.RED + "Pwing!");
					player.updateInventory();
				}
			}
    		} catch (Exception ex) {            } 
    	}
    	if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK){
    		try{
    			if (item.getType() == splodeBow){
    				plugin.arrowControl.checkArrows(true, player);
    			}
    		}catch (Exception ex){}
    	}
    }
    
   //public void onPlayerMove(PlayerMoveEvent event){
	   //plugin.arrowControl.checkArrows();
   //}
    
}
