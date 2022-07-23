package JFed9.Project;

import net.minecraft.world.level.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class Listeners implements Listener {

    @EventHandler
    public void breakBlock(BlockBreakEvent e) {
        CustomEntityZombie z = new CustomEntityZombie((World) e.getPlayer().getWorld());
        CustomEntityZombie.spawn(e.getPlayer().getLocation());
    }

}
