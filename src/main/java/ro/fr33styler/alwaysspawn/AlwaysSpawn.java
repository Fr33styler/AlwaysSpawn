package ro.fr33styler.alwaysspawn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class AlwaysSpawn extends JavaPlugin implements Listener {

    private static final String PERMISSION_SPAWN_SET = "alwaysspawn.set";

    public Location spawn;

    @Override
    public void onEnable() {
        loadSpawn();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSpawn(PlayerSpawnLocationEvent e) {
        if (spawn != null) {
            e.setSpawnLocation(spawn);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRespawn(PlayerRespawnEvent e) {
        if (spawn != null) {
            e.setRespawnLocation(spawn);
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length > 0) {
            commandSender.sendMessage(ChatColor.RED + "Invalid arguments!");
        } else if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Must be executed by a player!");
        } else if (commandSender.hasPermission(PERMISSION_SPAWN_SET)) {
            saveSpawn(spawn = ((Player) commandSender).getLocation());
            commandSender.sendMessage(ChatColor.GREEN + "Spawn successfully set!");
            return true;
        }
        return false;
    }

    private void loadSpawn() {
        ConfigurationSection section = getConfig().getConfigurationSection("Spawn");
        if (section != null) {
            World world = Bukkit.getWorld(section.getString("World"));
            double x = section.getDouble("X");
            double y = section.getDouble("Y");
            double z = section.getDouble("Z");
            float yaw = (float) section.getDouble("Yaw");
            float pitch = (float) section.getDouble("Pitch");
            spawn = new Location(world, x, y, z, yaw, pitch);
        }
    }

    private void saveSpawn(Location spawn) {
        ConfigurationSection section = getConfig().createSection("Spawn");
        section.set("World", spawn.getWorld().getName());
        section.set("X", spawn.getX());
        section.set("Y", spawn.getY());
        section.set("Z", spawn.getZ());
        section.set("Yaw", spawn.getYaw());
        section.set("Pitch", spawn.getPitch());
        saveConfig();
    }

}
