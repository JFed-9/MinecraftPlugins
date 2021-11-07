package JFed9.ToolSaver;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class BlockBreakListener implements Listener {

    double dangerzone = 0.15;

    @EventHandler
    public void onBreakEvent(BlockBreakEvent e) {
//        Bukkit.broadcastMessage("Checking Block");
        ItemStack tool = e.getPlayer().getInventory().getItemInMainHand();
        ItemMeta metadata = tool.getItemMeta();
        if (metadata instanceof Damageable) {
//            Bukkit.broadcastMessage("Damage: " + (((Damageable) metadata).getDamage()));
//            Bukkit.broadcastMessage("Percentage: " + 1.0 * (((Damageable) metadata).getDamage()) / tool.getType().getMaxDurability());
            if (tool.getType().getMaxDurability() - (((Damageable) metadata).getDamage()) == Math.floor(tool.getType().getMaxDurability() * dangerzone) && tool.getType().getMaxDurability() > 1) {
                e.getPlayer().sendMessage("Your " + tool.getType().name() + " is about to break!!");
            }
        }
    }
}
