package JFed9.MinecraftBlockAbsorb;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    Listener listener = new MovementListener();

    @Override
    public void onEnable() {
        getLogger().info("BlockAbsorb Enabled");
        Bukkit.getServer().getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("BlockAbsorb Disabled");
        PlayerMoveEvent.getHandlerList().unregister(listener);
    }
}
