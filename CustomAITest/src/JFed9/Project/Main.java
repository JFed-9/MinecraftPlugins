package JFed9.Project;

import net.minecraft.world.entity.monster.EntityZombie;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Custom Plugin Enabled");

        NMSUtil nmsUtil = new NMSUtil();
        nmsUtil.registerEntity("Flash", 54, EntityZombie.class, CustomEntityZombie.class);

        Bukkit.getPluginManager().registerEvents(new Listeners(), this);

    }

    @Override
    public void onDisable() {
        getLogger().info("Custom Plugin Disabled");
    }
}
