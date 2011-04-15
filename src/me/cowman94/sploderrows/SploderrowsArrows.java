package me.cowman94.sploderrows;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.*;
import me.cowman94.sploderrows.*;
import org.bukkit.entity.*;
public class SploderrowsArrows {
public Arrow arrow;
public Player owner;
public Location lastPostion;
public boolean setBlockOnFire;
public boolean waitTillNextCall;
public boolean tactical;

public SploderrowsArrows(Player p, Arrow a, boolean block, Sploderrows plugin) {
owner = p;
arrow = a;
setBlockOnFire = block;
lastPostion = p.getLocation();
waitTillNextCall = true;
tactical = owner.isSneaking();
System.out.println(a.getEntityId());
if (block){a.setFireTicks(600);}
plugin.arrowControl.addArrow(this, block);

}
}
