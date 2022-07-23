package JFed9.Generations;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class Main extends JavaPlugin {

    PlayerManager pm;

    @Override
    public void onEnable() {
        getLogger().info("Generations Enabled");
        // Initialize PlayerManager
        pm = PlayerManager.getInstance();
        pm.initialize(this);
        PluginCommand command;
        command = getCommand("startGames");
        if (command != null) command.setExecutor(pm);
    }

    @Override
    public void onDisable() {
        try {
            pm.saveAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getLogger().info("Generations Disabled");
    }
}
