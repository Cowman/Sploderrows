package me.cowman94.sploderrows;


import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class SploderrowsDebugCommand implements CommandExecutor {
    private final Sploderrows plugin;

    public SploderrowsDebugCommand(Sploderrows plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            plugin.setDebugging(player, !plugin.isDebugging(player));

            return true;
        } else {
            return false;
        }
    }
}
