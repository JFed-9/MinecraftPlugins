package JFed9.Project;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class Listeners implements Listener {

    private Map<Enchantment, Integer> enchants;

    @EventHandler
    public void a(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getType() == InventoryType.GRINDSTONE && event.getSlotType() == InventoryType.SlotType.RESULT) {
            Bukkit.getServer().broadcastMessage("CurrentItem:" + event.getCurrentItem().toString());
            Bukkit.getServer().broadcastMessage("Name:" + event.getWhoClicked().getName());
            Bukkit.getServer().broadcastMessage("Action:" + event.getAction());
            Bukkit.getServer().broadcastMessage("Enchants:" + event.getCurrentItem().getEnchantments());
            ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta itemMeta = book.hasItemMeta() ? book.getItemMeta() : Bukkit.getItemFactory().getItemMeta(book.getType());
            Bukkit.getServer().broadcastMessage("Getting book ready");
            if (itemMeta == null) return;
            Bukkit.getServer().broadcastMessage("Adding enchants");
            if (enchants == null || enchants.isEmpty()) return;
            book.addEnchantments(enchants);
            Bukkit.getServer().broadcastMessage("Restoring Metas");
            book.setItemMeta(itemMeta);
            Bukkit.getServer().broadcastMessage("Finishing up");
            event.getWhoClicked().getInventory().addItem(book);
        }
    }


    @EventHandler
    public void b(InventoryEvent event) {
        Bukkit.getServer().broadcastMessage("InventoryEvent");
        Bukkit.getServer().broadcastMessage(String.valueOf(event.getInventory().getType()));
        Bukkit.getServer().broadcastMessage(event.getEventName());
    }

    @EventHandler
    public void c(InventoryMoveItemEvent event) {
        Bukkit.getServer().broadcastMessage("InventoryMoveItemEvent");
        Bukkit.getServer().broadcastMessage(String.valueOf(event.getDestination().getType()));
        Bukkit.getServer().broadcastMessage(String.valueOf(event.getItem()));
        Bukkit.getServer().broadcastMessage(String.valueOf(event.getSource().getType()));
    }

    @EventHandler
    public void d(InventoryDragEvent event) {
        Bukkit.getServer().broadcastMessage("InventoryDragEvent");
        Bukkit.getServer().broadcastMessage(event.getNewItems().keySet().toString());
    }

    @EventHandler
    public void e(InventoryCreativeEvent event) {
        Bukkit.getServer().broadcastMessage("InventoryCreativeEvent");
    }

    @EventHandler
    public void f(InventoryClickEvent event) {
        Bukkit.getServer().broadcastMessage("InventoryClickEvent");
        Bukkit.getServer().broadcastMessage(String.valueOf(event.getCurrentItem()));
        Bukkit.getServer().broadcastMessage(String.valueOf(event.getClick()));
        Bukkit.getServer().broadcastMessage(String.valueOf(event.getSlotType()));
        Bukkit.getServer().broadcastMessage(String.valueOf(event.getResult()));

    }

}
