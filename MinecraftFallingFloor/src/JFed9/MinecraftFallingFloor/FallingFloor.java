package JFed9.MinecraftFallingFloor;

import org.bukkit.plugin.java.JavaPlugin;

public class FallingFloor extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("FallingFloor Enabled");
        getServer().getPluginManager().registerEvents(new Listeners(),this);
    }

    @Override
    public void onDisable() {
        getLogger().info("FallingFloor Disabled");
    }
}
