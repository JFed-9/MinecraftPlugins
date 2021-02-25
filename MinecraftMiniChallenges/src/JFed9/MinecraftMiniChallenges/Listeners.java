package JFed9.MinecraftMiniChallenges;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

import static JFed9.MinecraftMiniChallenges.ChallengeCodes.*;

public class Listeners extends ChallengeWithListener {

    @Override
    public String getTaskDescription(int code) {
       switch (code) {
           case NETHER_ENTER:
               return "Reach the Nether";
           case ENDER_EYE_GET:
               return "Craft an Eye of Ender";
           case FIND_DIAMONDS:
               return "Mine a diamond";
           case ENCHANT:
               return "Enchant something";
           case ANCIENT_DEBRIS:
               return "Find Ancient Debris";
           case KILL_BLAZE:
               return "Kill a Blaze";
           case KILL_GHAST:
               return "Kill a Ghast";
           case REACH_256:
               return "Climb to the build limit";
           case NETHER_ROOF:
               return "Get on top of the Nether roof";
           case GHAST_CAPTURE:
               return "Capture a Ghast in a minecart";
           case END_ENTER:
               return "Reach The End";
           case BUILD_SNOWMAN:
               return "Build a Snow Golem";
           case MUSIC_DISK_GET:
               return "Obtain a music disc";
           case SPAWN_WITHER:
               return "Spawn a Wither (be careful!)";
           case EAT_STEAK:
               return "Eat a steak";
           case 0:
               return "";
           default:
               return "Error, invalid task";
       }
    }

    @Override
    public int getPointValue(int code) {
        switch (code) {
            case NETHER_ENTER:
                return 5;
            case ENDER_EYE_GET:
            case SPAWN_WITHER:
                return 20;
            case FIND_DIAMONDS:
                return 8;
            case ENCHANT:
                return 12;
            case ANCIENT_DEBRIS:
                return 25;
            case KILL_BLAZE:
                return 13;
            case KILL_GHAST:
                return 7;
            case REACH_256:
                return 3;
            case NETHER_ROOF:
                return 14;
            case GHAST_CAPTURE:
                return 30;
            case END_ENTER:
                return 40;
            case BUILD_SNOWMAN:
                return 10;
            case MUSIC_DISK_GET:
                return 18;
            case EAT_STEAK:
                return 7;
            default:
                return 0;
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (event.getRecipe().getResult().getType().equals(Material.ENDER_EYE))
            onComplete(ENDER_EYE_GET, Bukkit.getPlayer(event.getWhoClicked().getName()));
    }

    @EventHandler
    public void onMineItem(BlockBreakEvent event) {
        if (event.getBlock().getType().equals(Material.DIAMOND_ORE))
            onComplete(FIND_DIAMONDS,event.getPlayer());
        if (event.getBlock().getType().equals(Material.ANCIENT_DEBRIS))
            onComplete(ANCIENT_DEBRIS,event.getPlayer());
        if (event.getBlock().getType().equals(Material.DIORITE))
            onComplete(DIORITE_BREAK,event.getPlayer());
    }

    @EventHandler
    public void onEatItem(PlayerItemConsumeEvent event) {
        if (event.getItem().getType().equals(Material.COOKED_BEEF))
            onComplete(EAT_STEAK, event.getPlayer());
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
         onComplete(ENCHANT, event.getEnchanter());
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Blaze)
            if (event.getEntity().getKiller() != null)
                onComplete(KILL_BLAZE, event.getEntity().getKiller());
        if (event.getEntity() instanceof Ghast)
            if (event.getEntity().getKiller() != null)
                onComplete(KILL_GHAST, event.getEntity().getKiller());
        if (event.getEntity() instanceof Creeper)
            for (Material disc : Arrays.asList(
                    Material.MUSIC_DISC_11,
                    Material.MUSIC_DISC_13,
                    Material.MUSIC_DISC_BLOCKS,
                    Material.MUSIC_DISC_CAT,
                    Material.MUSIC_DISC_CHIRP,
                    Material.MUSIC_DISC_FAR,
                    Material.MUSIC_DISC_MALL,
                    Material.MUSIC_DISC_MELLOHI,
                    Material.MUSIC_DISC_PIGSTEP,
                    Material.MUSIC_DISC_STAL,
                    Material.MUSIC_DISC_STRAD,
                    Material.MUSIC_DISC_WAIT,
                    Material.MUSIC_DISC_WARD))
                if (event.getDrops().contains(new ItemStack(disc)))
                    onComplete(MUSIC_DISK_GET,getClosestPlayer(event.getEntity().getLocation()));
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Location location = event.getTo();
        if (location == null) return;
        if (location.getY() >= 256)
            onComplete(REACH_256,event.getPlayer());
        World world = location.getWorld();
        if (world == null) return;
        World.Environment environment = world.getEnvironment();
        if (environment.equals(World.Environment.NETHER)) {
            onComplete(NETHER_ENTER, event.getPlayer());
            if (location.getY()>=128)
                onComplete(NETHER_ROOF, event.getPlayer());
        }
        if (environment.equals(World.Environment.THE_END))
            onComplete(END_ENTER, event.getPlayer());
    }

    @EventHandler
    public void onPickup(InventoryCloseEvent event) {
        Inventory itemStacks = event.getPlayer().getInventory();
        if (itemStacks.contains(Material.ANCIENT_DEBRIS)) onComplete(ANCIENT_DEBRIS,Bukkit.getPlayer(event.getPlayer().getUniqueId()));
        for (Material disc : Arrays.asList(
                Material.MUSIC_DISC_11,
                Material.MUSIC_DISC_13,
                Material.MUSIC_DISC_BLOCKS,
                Material.MUSIC_DISC_CAT,
                Material.MUSIC_DISC_CHIRP,
                Material.MUSIC_DISC_FAR,
                Material.MUSIC_DISC_MALL,
                Material.MUSIC_DISC_MELLOHI,
                Material.MUSIC_DISC_PIGSTEP,
                Material.MUSIC_DISC_STAL,
                Material.MUSIC_DISC_STRAD,
                Material.MUSIC_DISC_WAIT,
                Material.MUSIC_DISC_WARD))
            if (itemStacks.contains(disc)) onComplete(MUSIC_DISK_GET,Bukkit.getPlayer(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        Entity entity = event.getEntered();
        if (entity.getType().equals(EntityType.GHAST))
            onComplete(GHAST_CAPTURE,getClosestPlayer(entity.getLocation()));
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.BUILD_SNOWMAN))
            onComplete(BUILD_SNOWMAN,getClosestPlayer(event.getLocation()));
        if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.BUILD_WITHER))
            onComplete(SPAWN_WITHER,getClosestPlayer(event.getLocation()));

    }

    private Player getClosestPlayer(Location loc) {
        Player result = null;
        double lastDistance = Double.MAX_VALUE;
        for(Player p : Bukkit.getOnlinePlayers()) {
            if (p.getLocation().equals(loc)) continue;

            double distance = loc.distance(p.getLocation());
            if(distance < lastDistance) {
                lastDistance = distance;
                result = p;
            }
        }
        return result;
    }
}
