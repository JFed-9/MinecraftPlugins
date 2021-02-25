package JFed9.MinecraftFallingFloor;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Listeners implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event == null || event.getTo() == null) return;
        if (sameBlock(event.getFrom(),event.getTo())) return;
        World world = event.getFrom().getWorld();
        if (world == null) return;
        int x = event.getFrom().getBlockX();
        int y = 1;
        int z = event.getFrom().getBlockZ();

        for (; y <= event.getFrom().getBlockY(); y++) {
            Block block = world.getBlockAt(x, y, z);
//            block.setType(block.getType(), true);
            //I'd rather just add gravity to the block, but I can't get that to work
            if (!partOfPortal(block)) block.breakNaturally();
        }
    }

    private boolean sameBlock(Location l1, Location l2) {
        return (int) l1.getZ() == (int) l2.getZ() && (int)l1.getX() == (int)l2.getX();
    }

    private boolean partOfPortal(Block block) {
        if (block.getType() != Material.OBSIDIAN && block.getType() != Material.NETHER_PORTAL) return false;
        if (block.getType() == Material.NETHER_PORTAL) return true;
        return block.getWorld().getBlockAt(block.getX(), block.getY() + 1, block.getZ()).getType() == Material.NETHER_PORTAL ||
                block.getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ()).getType() == Material.NETHER_PORTAL ||
                block.getWorld().getBlockAt(block.getX() + 1, block.getY(), block.getZ()).getType() == Material.NETHER_PORTAL ||
                block.getWorld().getBlockAt(block.getX() - 1, block.getY(), block.getZ()).getType() == Material.NETHER_PORTAL ||
                block.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ() + 1).getType() == Material.NETHER_PORTAL ||
                block.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ() - 1).getType() == Material.NETHER_PORTAL;
        //Note: This will not erase blocks that are next to a portal that aren't essential to that portal. I don't think that's a huge deal.
    }
}

