package me.cowman94.sploderrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.HashMap.*;
import javax.swing.JOptionPane;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

public class Sploderrows extends JavaPlugin{
	private final SploderrowsEntityListener entityListener = new SploderrowsEntityListener(this);
	private static final Logger log = Logger.getLogger("Minecraft");
	private final SploderrowsBlockListener blockListener = new SploderrowsBlockListener(this);
	private final SploderrowsPlayerListener playerListener = new SploderrowsPlayerListener(this);
	public final SploderrowsArrowControl arrowControl = new SploderrowsArrowControl(this);
	public final SploderrowsSplode sploder = new SploderrowsSplode(this);
	public final HashMap<Player,ArrayList<Block>> sploderrowsUsers = new HashMap<Player, ArrayList<Block>>();
	private final HashMap<Player, Boolean> instaUsers = new HashMap<Player, Boolean>();
	Configuration config;
	public static Sploderrows sploderrows;
	public boolean markedForReset = false;
	
	int intvar;
	double doublevar;
	boolean boolvar;
	
	public void readConfig() {
        String dir = "plugins/Sploderrows/";
        String filename = "config.yml";
   File file = new File(dir);
   if (!(file.exists())){
   file.mkdir();
   }
   file = new File(dir + filename);
        if (!file.exists()){
        try{
        // Create file
        FileWriter fstream = new FileWriter(dir + filename);
        BufferedWriter out = new BufferedWriter(fstream);
        List<World> worlds = this.getServer().getWorlds();
        for (int i = 0; i<worlds.size();i++){
        out.write(worlds.get(i).getName() + ":\n");
        out.write(" Default:\n");
        out.write(" splodebow: 289\n");
        out.write(" firebow: 340\n");
        out.write(" midair: true\n");
        out.write(" fireradius: 1\n");
        out.write(" splodecost: 5\n");
        out.write(" firecost: 3\n");
        out.write(" splodeforce: 6.0\n");
        out.write(" tacticalforce: 3.0\n\n");
        }
        //Close the output stream
        out.close();
        }catch (Exception e){//Catch exception if any
        System.out.println("Sploderrows could not write the default config file.");
        }
        }
        config = new Configuration(file);
        config.load();
        markedForReset = testConfig();
       }
       
       public void reloadConfig(Player caller) {
        config.load();
        markedForReset = testConfig();
        if (markedForReset){
        	(caller).sendMessage("Incorrect syntax in ");
        }
       }
           
	public boolean testConfig(){
        List<World> worlds = this.getServer().getWorlds();
        try{
        for (int i = 0; i<worlds.size();i++){
		
		getInt(worlds.get(i), "splodeBow");
		getInt(worlds.get(i), "fireBow");
		getInt(worlds.get(i), "fireRadius");
		getInt(worlds.get(i), "splodeCost");
		getInt(worlds.get(i), "fireCost");
		getBool(worlds.get(i), "midAir");
		getFloat(worlds.get(i), "splodeForce");
		getFloat(worlds.get(i), "tacticalForce");
		
		}return false;}catch (Exception itn){
			return true;
		}
        
	}
	
	public void onEnable(){
		readConfig();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_PICKUP_ITEM, playerListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_PHYSICS, blockListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_CANBUILD, blockListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_DAMAGE, blockListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_COMBUST, entityListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_DEATH, entityListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_ITEM_HELD, playerListener, Event.Priority.Normal, this);
        log.info("Sploderrows v 0.8 is on");
        sploderrows = this;
        long delay = 20L; // 20 ticks = 1 second
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new SAThread(), 0L, 5L);
	}
	public void onDisable(){
		log.info("Sploderrows v 0.8 is off");
		}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(commandLabel.equalsIgnoreCase("splode")){
			//toggleSploderrows((Player) sender);
			//return true;
			String world;
			try{
			if (args[0].equalsIgnoreCase("save")){
				config.save();
				((Player) sender).sendMessage(ChatColor.GREEN + "Configuration Saved.");
			}else if (args[0].equalsIgnoreCase("reset")){
				if (args.length > 1 || markedForReset){
					rewriteConfig();
					((Player) sender).sendMessage(ChatColor.LIGHT_PURPLE + "Configuration Reset.");
				}
			} else
			if (args[0].equalsIgnoreCase("help")){
				((Player) sender).sendMessage(ChatColor.DARK_PURPLE + "This command is used to change the settings for Sploderrows.");
				((Player) sender).sendMessage(ChatColor.RED + "/splode set <variable> <value>");((Player) sender).sendMessage(ChatColor.AQUA + "	Sets <variable> to <value>, must be same correct type.");
				((Player) sender).sendMessage(ChatColor.RED + "/splode get <variable>");((Player) sender).sendMessage(ChatColor.AQUA + "	Returns the current value of <variable>.");
				((Player) sender).sendMessage(ChatColor.RED + "/splode reload" ); ((Player) sender).sendMessage(ChatColor.AQUA + "	Reloads the Sploderrows configuration file.");
				((Player) sender).sendMessage(ChatColor.RED + "/splode listvar" ); ((Player) sender).sendMessage(ChatColor.AQUA + "	Lists variables, and their types.");
				((Player) sender).sendMessage(ChatColor.RED + "/splode reset [Overide]"); ((Player) sender).sendMessage(ChatColor.AQUA + "	Resets configfile if corrupt.");
				((Player) sender).sendMessage(ChatColor.AQUA + "	Anything in Overide will reset the config no matter what.");
				((Player) sender).sendMessage(ChatColor.RED + "/splode save");
				((Player) sender).sendMessage(ChatColor.AQUA + "	Saves the configuration file.");
			}else if (args[0].equalsIgnoreCase("set")){
				try{
					System.out.println(Integer.parseInt(args[2]));
					intvar = Integer.parseInt(args[2]);
				} catch (Exception ex){}
					try {
						System.out.println(Double.parseDouble(args[2]));
						doublevar = Double.parseDouble(args[2]);
					} catch (Exception exe){}
						try{
							boolvar = Boolean.getBoolean(args[2]);
						} catch (Exception exep){ boolvar = (Boolean) null;}
				try{world = args[3];} catch (Exception exi){world = ((Player) sender).getWorld().getName();}	
				
				if(setVar(args[1],intvar,boolvar,(float)doublevar, (Player) sender, world)){
					((Player) sender).sendMessage(args[1] + " changed to " + args[2]);
					//reloadConfig((Player) sender);
				}
			}else if (args[0].equalsIgnoreCase("get")){
				World orld;
				try{orld = getWorldByName(args[2]);} catch (Exception exi){orld = ((Player) sender).getWorld();}
				if (args[1].equalsIgnoreCase("splodeBow")){
					((Player) sender).sendMessage(args[1] + "'s value is " + getInt(orld, "splodeBow"));
				}
				if (args[1].equalsIgnoreCase("fireBow")){
					((Player) sender).sendMessage(args[1] + "'s value is " + getInt(orld, "fireBow"));
				}
				if (args[1].equalsIgnoreCase("fireRadius")){
					((Player) sender).sendMessage(args[1] + "'s value is " + getInt(orld, "fireRadius"));
				}
				if (args[1].equalsIgnoreCase("splodeCost")){
					((Player) sender).sendMessage(args[1] + "'s value is " + getInt(orld, "splodeCost"));
				}
				if (args[1].equalsIgnoreCase("fireCost")){
					((Player) sender).sendMessage(args[1] + "'s value is " + getInt(orld, "fireCost"));
				}
				if (args[1].equalsIgnoreCase("midAir")){
					((Player) sender).sendMessage(args[1] + "'s value is " + getBool(orld, "midAir"));
				}
				if (args[1].equalsIgnoreCase("splodeForce")){
					((Player) sender).sendMessage(args[1] + "'s value is " + getFloat(orld, "splodeForce"));
				}
				if (args[1].equalsIgnoreCase("tacticalForce")){
					((Player) sender).sendMessage(args[1] + "'s value is " + getFloat(orld, "tacticalForce"));
			}else if (args[0].equalsIgnoreCase("reload")){
				reloadConfig((Player) sender);
			} else if (args[0].equalsIgnoreCase("listvar")){
				((Player) sender).sendMessage("splodeBow: integer");
				((Player) sender).sendMessage("fireBow: integer");
				((Player) sender).sendMessage("fireRadius: integer");
				((Player) sender).sendMessage("midAir: boolean");
				((Player) sender).sendMessage("splodeCost: integer");
				((Player) sender).sendMessage("fireCost: integer");
				((Player) sender).sendMessage("splodeForce: float (decimal) (also accepts integer)");
				((Player) sender).sendMessage("tacticalForce: float (decimal) (also accepts integer)");
			}}} catch (Exception exer){}
			return true;
		}else if (commandLabel.equalsIgnoreCase("inst")){
			toggleInstaBreak((Player) sender);
			return true;
		}
		return false;
		
	}
	private void toggleInstaBreak(Player player) {
		// TODO Auto-generated method stub
		if (godly(player)){
			this.instaUsers.remove(player);
			player.sendMessage("InstaBreak Disabled");
		}else{
			this.instaUsers.put(player, null);
			player.sendMessage("InstaBreak Enabled");
		}
	}

	public void toggleSploderrows(Player player){
		if(enabled(player)){
			this.sploderrowsUsers.remove(player);
			player.sendMessage("Sploderrows Disabled");
		}else{
			this.sploderrowsUsers.put(player, null);
			player.sendMessage("Sploderrows Enabled");
		}
	}
	public boolean enabled(Player player){
		return this.sploderrowsUsers.containsKey(player);
	}
	
    
    public boolean godly(final Player player) {
        if (instaUsers.containsKey(player)) {
            return true;
        } else {
            return false;
        }
    }

    
    
    public boolean deductArrows(Player player, int amount){
    	int amt = amount;
    	Inventory inventory = player.getInventory();
    	int arrtot = 0;
    	for (int i=0;i<36;i++){
    		if (inventory.getItem(i).getType() == Material.ARROW){arrtot += inventory.getItem(i).getAmount();}
    	}
    	if (arrtot >= amt){
    	while (amt > 0){
    		System.out.println(amt);
    		ItemStack replacer = inventory.getItem(inventory.first(Material.ARROW));
    		if (replacer.getAmount() <= amt){
    			amt -= replacer.getAmount();
    			inventory.remove(replacer);
    			//inventory.remove(inventory.first(Material.ARROW));
    		}else{
    			replacer.setAmount(replacer.getAmount()-amt);
    			inventory.setItem(inventory.first(Material.ARROW), replacer);
    			amt = 0;
    		}
    	}
    	return true;
    	}else{
    	return false;
    	}
    }
    
	private boolean setVar(String string, int intval, boolean boolval, float floatval, Player player, String world){
		if (string.equalsIgnoreCase("splodeForce") || string.equalsIgnoreCase("tacticalForce")){
	        try{
	        	this.config.setProperty(world + "." + string.toLowerCase(), doublevar);
	            }catch (Exception e){//Catch exception if any
	            	
	            System.out.println("Incorrect Variable Type");
	            return false;
	            }
	        }else if(string.equalsIgnoreCase("midAir")){
	    	        try{
	    	        	this.config.setProperty(world + "." + string.toLowerCase(), boolvar);
	    	            }catch (Exception e){//Catch exception if any
	    	            	
	    	            System.out.println("Incorrect Variable Type");
	    	            return false;
	    	            }
	            }else{
	    	        try{
	    	        	this.config.setProperty(world + "." + string.toLowerCase(), intvar);
	    	            }catch (Exception e){//Catch exception if any
	    	            	
	    	            System.out.println("Incorrect Variable Name or Type");
	    	            return false;
	    	            
	            }
			
			return true;
		}
		return false;
	}
	
    private class SAThread implements Runnable {
    	public void run() {
    	long time = System.currentTimeMillis() / 1000;
    	sploderrows.arrowControl.checkArrows(false, null);
    	}
    	}
    
    public int getInt(World world, String variable){
    	return this.config.getInt(world.getName() + "." + variable.toLowerCase(), -1);
    }
    public boolean getBool(World world, String variable){
    	return this.config.getBoolean(world.getName() + "." + variable.toLowerCase(), false);
    }
    public float getFloat(World world, String variable){
    	return (float) this.config.getDouble(world.getName() + "." + variable.toLowerCase(), -1.0);
    }
    public int getInt(Player world, String variable){
    	return this.config.getInt(world.getWorld().getName() + "." + variable.toLowerCase(), -1);
    }
    public boolean getBool(Player world, String variable){
    	return this.config.getBoolean(world.getWorld().getName() + "." + variable.toLowerCase(), false);
    }
    public float getFloat(Player world, String variable){
    	return (float) this.config.getDouble(world.getName() + "." + variable.toLowerCase(), -1.0);
    }
    
    public int getInt(String variable){
    	return this.config.getInt(variable, -1);
    }
    public boolean getBool(String variable){
    	return this.config.getBoolean(variable, false);
    }
    public float getFloat(String variable){
    	return (float) this.config.getDouble(variable, -1.0);
    }
    
    public World getWorldByName (String world){
    	List<World> wor = this.getServer().getWorlds();
    	for (int i=0;i<wor.size();i++){
    		if (wor.get(i).getName().equalsIgnoreCase(world)){
    			return wor.get(i);
    		}
    	}
    	return null;
    }
    
    public void rewriteConfig(){
        String dir = "plugins/Sploderrows/";
        String filename = "config.yml";
    	try{
            // Create file
            FileWriter fstream = new FileWriter(dir + filename);
            BufferedWriter out = new BufferedWriter(fstream);
            List<World> worlds = this.getServer().getWorlds();
            for (int i = 0; i<worlds.size();i++){
            out.write(worlds.get(i).getName() + ":\n");
            out.write(" Default:\n");
            out.write(" splodebow: 289\n");
            out.write(" firebow: 340\n");
            out.write(" midair: true\n");
            out.write(" fireradius: 1\n");
            out.write(" splodecost: 5\n");
            out.write(" firecost: 3\n");
            out.write(" splodeforce: 6.0\n");
            out.write(" tacticalforce: 3.0\n\n");
            }
            //Close the output stream
            out.close();
            }catch (Exception e){//Catch exception if any
            System.out.println("Sploderrows could not write the default config file.");
            }
    }
}
