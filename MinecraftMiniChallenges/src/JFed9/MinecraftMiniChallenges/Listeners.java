package JFed9.MinecraftMiniChallenges;

import javafx.util.Pair;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static JFed9.MinecraftMiniChallenges.ChallengeCodes.*;

public class Listeners extends ChallengeWithListener {

    Map<Pair<Integer,UUID>, Integer> taskDetails = new HashMap<>();

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
           case KILL_WITHER:
               return "Spawn a Wither (be careful!)";
           case EAT_STEAK:
               return "Eat a steak";
           case GET_COPPER_BLOCK:
               return "Obtain a block of copper";
           case ENTER_GEODE:
               return "Find a Geode and get inside";
           case GOAT_MASSACRE:
               return "Kill 10 goats";
           case AXOLOTL_CAPTURE:
               return "Capture an axolotl in a bucket";
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
            case GET_COPPER_BLOCK:
                return 5;
            case ENDER_EYE_GET:
            case KILL_WITHER:
                return 100;
            case FIND_DIAMONDS:
            case AXOLOTL_CAPTURE:
                return 15;
            case ENCHANT:
            case GOAT_MASSACRE:
                return 20;
            case ANCIENT_DEBRIS:
                return 25;
            case KILL_BLAZE:
                return 13;
            case KILL_GHAST:
            case EAT_STEAK:
                return 7;
            case REACH_256:
                return 3;
            case NETHER_ROOF:
                return 14;
            case GHAST_CAPTURE:
                return 150;
            case END_ENTER:
                return 40;
            case BUILD_SNOWMAN:
            case ENTER_GEODE:
                return 10;
            case MUSIC_DISK_GET:
                return 18;
            default:
                return 0;
        }
    }

    public Function<Void, Void> getPrepWork(int code) {
        switch (code) {
            case KILL_WITHER:
                return Void -> {
                    ItemStack[] items = {
                            new ItemStack(Material.WITHER_SKELETON_SKULL,3),
                            new ItemStack(Material.SOUL_SAND,4)
                    };
                    for (Player p : Bukkit.getOnlinePlayers())
                        p.getInventory().addItem(items);
                    return null;
                };
            case KILL_BLAZE:
                return Void -> {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        Location loc;
                        World world = null;
                        for (World w : Bukkit.getWorlds()) {
                            if (w.getEnvironment().equals(World.Environment.NETHER)) {
                                world = w;
                                break;
                            }
                        }
                        if (world == null) {
                            Bukkit.broadcastMessage("Nether has not been loaded");
                            return null;
                        }
                        if (p.getWorld().getEnvironment() == World.Environment.NETHER)
                            loc = p.getLocation();
                        else
                            loc = world.getSpawnLocation();
                        p.sendMessage("Nearest fortress: " + world.locateNearestStructure(loc, StructureType.NETHER_FORTRESS,100,false));
                    }
                    return null;
                };
            case NETHER_ENTER:
            case GET_COPPER_BLOCK:
            case ENDER_EYE_GET:
            case FIND_DIAMONDS:
            case AXOLOTL_CAPTURE:
            case ENCHANT:
            case GOAT_MASSACRE:
            case ANCIENT_DEBRIS:
            case KILL_GHAST:
            case EAT_STEAK:
            case REACH_256:
            case NETHER_ROOF:
            case GHAST_CAPTURE:
            case END_ENTER:
            case BUILD_SNOWMAN:
            case ENTER_GEODE:
            case MUSIC_DISK_GET:
            default:
                return null;
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (event.getRecipe().getResult().getType().equals(Material.ENDER_EYE))
            onComplete(ENDER_EYE_GET, Bukkit.getPlayer(event.getWhoClicked().getName()));
        if (event.getRecipe().getResult().getType().equals(Material.COPPER_BLOCK))
            onComplete(GET_COPPER_BLOCK, Bukkit.getPlayer(event.getWhoClicked().getName()));
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
        if (event.getEntity() instanceof Goat)
            if (event.getEntity().getKiller() != null) {
                if (taskDetails.containsKey(new Pair<>(GOAT_MASSACRE,event.getEntity().getKiller().getUniqueId())))
                    taskDetails.put(new Pair<>(GOAT_MASSACRE,event.getEntity().getKiller().getUniqueId()),taskDetails.get(new Pair<>(GOAT_MASSACRE,event.getEntity().getKiller().getUniqueId()))+1);
                else
                    taskDetails.put(new Pair<>(GOAT_MASSACRE,event.getEntity().getKiller().getUniqueId()),1);
                if (taskDetails.get(new Pair<>(GOAT_MASSACRE,event.getEntity().getKiller().getUniqueId())) >= 10)
                    onComplete(GOAT_MASSACRE, event.getEntity().getKiller());
            }
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
        for (Material block : Arrays.asList(
                Material.AMETHYST_BLOCK,
                Material.AMETHYST_CLUSTER,
                Material.AMETHYST_SHARD,
                Material.BUDDING_AMETHYST,
                Material.SMALL_AMETHYST_BUD,
                Material.MEDIUM_AMETHYST_BUD,
                Material.LARGE_AMETHYST_BUD))
            if (world.getBlockAt(event.getTo().getBlockX(),event.getTo().getBlockY()-1,event.getTo().getBlockZ()).getType().equals(block)) onComplete(ENTER_GEODE,Bukkit.getPlayer(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onPickup(InventoryCloseEvent event) {
        Inventory itemStacks = event.getPlayer().getInventory();
        if (itemStacks.contains(Material.ANCIENT_DEBRIS)) onComplete(ANCIENT_DEBRIS,Bukkit.getPlayer(event.getPlayer().getUniqueId()));
        if (itemStacks.contains(Material.COPPER_BLOCK)) onComplete(GET_COPPER_BLOCK,Bukkit.getPlayer(event.getPlayer().getUniqueId()));
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
//        if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.BUILD_WITHER))
//            onComplete(SPAWN_WITHER,getClosestPlayer(event.getLocation()));

    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        if (event.getBucket().equals(Material.AXOLOTL_BUCKET))
            onComplete(AXOLOTL_CAPTURE,Bukkit.getPlayer(event.getPlayer().getUniqueId()));
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
