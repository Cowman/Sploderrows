package me.cowman94.sploderrows;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import java.util.logging.Logger;

public class SploderrowsBlockListener extends BlockListener{
public static Sploderrows plugin;
private static final Logger log = Logger.getLogger("Minecraft");
public SploderrowsBlockListener(Sploderrows instance){
	plugin = instance;
	
}
public void onBlockBreak(BlockBreakEvent event){
	Block block = event.getBlock();
	Player player = event.getPlayer();
	LivingEntity that = event.getPlayer();
	if(block.getType() == Material.BEDROCK){
		player.sendMessage(ChatColor.RED + "There's A Glitch In The Matrix!");
		log.info("There's A Glitch In The Matrix!");
	}
}

public void onBlockDamage(BlockDamageEvent event){
	Player player = event.getPlayer();
	if (plugin.godly(player)){
		event.setInstaBreak(true);
	}
}
}

