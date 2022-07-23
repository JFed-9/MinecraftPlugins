package JFed9.ToolSaver;

import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    Listener listener;

    @Override
    public void onEnable() {
        getLogger().info("Custom Plugin Enabled");
        listener = new BlockBreakListener();
        getServer().getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void onDisable() {
        BlockBreakEvent.getHandlerList().unregister(listener);
        getLogger().info("Custom Plugin Disabled");
    }
}
