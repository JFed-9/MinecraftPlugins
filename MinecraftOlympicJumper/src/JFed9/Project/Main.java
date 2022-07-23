package JFed9.Project;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Custom Plugin Enabled");
        getServer().getPluginManager().registerEvents(new Listeners(),this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Custom Plugin Disabled");
    }
}
