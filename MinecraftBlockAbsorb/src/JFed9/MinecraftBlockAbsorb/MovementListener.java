package JFed9.MinecraftBlockAbsorb;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class MovementListener implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Location fromLoc = e.getFrom(), toLoc = e.getTo();
        if (toLoc == null) return;
        if (fromLoc.getBlockX() == toLoc.getBlockX() && fromLoc.getBlockZ() == toLoc.getBlockZ()) return;
        World world = e.getTo().getWorld();
        if (world == null) return;
        Block block = world.getBlockAt(toLoc.getBlockX(),toLoc.getBlockY() - 1, toLoc.getBlockZ());
        e.getPlayer().getInventory().addItem(new ItemStack(block.getType(),1));
    }
}
