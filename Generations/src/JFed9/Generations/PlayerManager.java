package JFed9.Generations;

import net.minecraft.world.level.block.state.BlockBase;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static org.bukkit.block.Biome.*;

public class PlayerManager implements CommandExecutor, Listener {

    private Plugin plugin;

    private Scoreboard scoreboard;
    
    private Map<String, String> playerNames;
    private List<String> usedCoords;
    private Map<String, Integer> playerCoords;

    private final String GREEN = "VeryAlive";
    private final String YELLOW = "KindaLiving";
    private final String RED = "AlmostDead";

    private static PlayerManager instance;
    public static PlayerManager getInstance() {
        if (instance == null) instance = new PlayerManager();
        return instance;
    }

    public PlayerManager() {
        playerNames = new HashMap<>();
        usedCoords = new LinkedList<>();
        playerCoords = new HashMap<>();
    }
    
    public void initialize(Plugin plug) {
        plugin = plug;
        try {
            scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
            if (scoreboard.getTeam(GREEN) == null) {
                scoreboard.registerNewTeam(GREEN);
                scoreboard.getTeam(GREEN).setColor(ChatColor.GREEN);
                scoreboard.getTeam(GREEN).setAllowFriendlyFire(true);
            }
            if (scoreboard.getTeam(YELLOW) == null) {
                scoreboard.registerNewTeam(YELLOW);
                scoreboard.getTeam(YELLOW).setColor(ChatColor.YELLOW);
                scoreboard.getTeam(YELLOW).setAllowFriendlyFire(true);
            }
            if (scoreboard.getTeam(RED) == null) {
                scoreboard.registerNewTeam(RED);
                scoreboard.getTeam(RED).setColor(ChatColor.RED);
                scoreboard.getTeam(RED).setAllowFriendlyFire(true);
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        
        File playerStatsFile = new File("ps.gen"),
                usedCoordsFile = new File("uc.gen"),
                playerCoordsFile = new File("pc.gen");
        
        //  -------------- PREPARE PLAYER NAMES -------------
        if (playerStatsFile.exists()) {
            Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
            System.out.println("Found existing colors file, gathering info");
            // Grab data from log file and initialize maps
            try {
                Scanner s = new Scanner(playerStatsFile);
                while (s.hasNextLine()) {
                    String line = s.nextLine();
                    String name = line.split(" ")[0],
                            team = line.split(" ")[1];
                    playerNames.put(name, team);
                    System.out.println(name + " should be " + team + " now.");
                    scoreboard.getTeam(playerNames.get(name)).addEntry(name);
                }
                s.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        // Go through online players and look for any new ones
        for (Player p : Bukkit.getOnlinePlayers())
            if (p != null && !playerNames.containsKey(p.getName())) {
                playerNames.put(Objects.requireNonNull(p.getPlayer()).getName(), GREEN);
                System.out.println("Found new player: " + p.getName());
            }

        //  -------------- PREPARE USED COORDS --------------
        if (usedCoordsFile.exists()) {
            System.out.println("Found existing coords file");
            // Grab used coords from file
            try {
                Scanner s = new Scanner(usedCoordsFile);
                while (s.hasNextLine()) {
                    String line = s.nextLine();
                    usedCoords.add(line);
                }
                s.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        //  ------------- PREPARE PLAYER COORDS -------------
        if (playerCoordsFile.exists()) {
            System.out.println("Found existing player coords file");
            try {
                Scanner s = new Scanner(playerCoordsFile);
                while (s.hasNextLine()) {
                    String line = s.nextLine();
                    String name = line.split(" ")[0],
                            index = line.split(" ")[1];
                    playerCoords.put(name, Integer.valueOf(index));
                    System.out.println(name + " has died as a red " + index + " times.");
                }
                s.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        // Grab all new players
        for (Player p : Bukkit.getOnlinePlayers())
            if (p != null && !playerCoords.containsKey(p.getName())) {
                playerCoords.put(Objects.requireNonNull(p.getPlayer()).getName(), 0);
                System.out.println(p.getName() + " is new, has not died");
            }
    }

    public void saveAll() throws IOException {
        System.out.println("Saving all data");
        PrintWriter prw = new PrintWriter("ps.gen");
        for (Map.Entry<String, String> entry : playerNames.entrySet()) {
            prw.println(entry.getKey() + " " + entry.getValue());
        }
        prw.close();
        prw = new PrintWriter("uc.gen");
        for (String coord : usedCoords)
            prw.println(coord.split(" ")[0] + " " + coord.split(" ")[1]);
        prw.close();
        prw = new PrintWriter("pc.gen");
        for (Map.Entry<String,Integer> entry : playerCoords.entrySet())
            prw.println(entry.getKey() + " " + entry.getValue());
        prw.close();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        getInstance();
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

        playerNames.clear();
        playerCoords.clear();
        usedCoords.clear();

        Player player = Bukkit.getPlayer(commandSender.getName());
        Location location;
        if (player == null) {
            location = commandSender.getServer().getWorlds().get(0).getSpawnLocation();
        } else {
            location = player.getLocation();
        }
        usedCoords.add(location.getBlockX() + " " + location.getBlockZ());

        for (Player p : Bukkit.getOnlinePlayers()) {
            System.out.println("Setting Spawn");
            if (location.getWorld() != null)
                location.getWorld().setSpawnLocation(location);
            p.setBedSpawnLocation(location);

            System.out.println("Changing ChatColor");
            playerNames.put(p.getName(), GREEN);
            playerCoords.put(p.getName(), 0);
            scoreboard.getTeam(GREEN).addEntry(p.getName());
        }
        return true;
    }
    
    private String getNextCoords(int x, int z) {
        Random rand = new Random();
        int newX = 0, newZ = 0;
        Set<Biome> bad = new HashSet<>();
        bad.add(OCEAN);
        bad.add(DEEP_OCEAN);
        bad.add(COLD_OCEAN);
        bad.add(DEEP_COLD_OCEAN);
        bad.add(LUKEWARM_OCEAN);
        bad.add(DEEP_LUKEWARM_OCEAN);
        bad.add(FROZEN_OCEAN);
        bad.add(DEEP_FROZEN_OCEAN);
        bad.add(WARM_OCEAN);
        bad.add(RIVER);
        while ((newX == 0 && newZ == 0) || bad.contains(Bukkit.getWorlds().get(0).getBiome(newX, 60, newZ))) {
            if (newX == 0 && newZ == 0)
                System.out.println("Finding new Coords");
            else {
                switch (Bukkit.getWorlds().get(0).getBiome(newX, 60, newZ)) {
                    case OCEAN:
                        System.out.println("Whoops, found a Ocean");
                        break;
                    case DEEP_OCEAN:
                        System.out.println("Whoops, found a Deep Ocean");
                        break;
                    case COLD_OCEAN:
                        System.out.println("Whoops, found a Cold Ocean");
                        break;
                    case DEEP_COLD_OCEAN:
                        System.out.println("Whoops, found a Deep Cold Ocean");
                        break;
                    case LUKEWARM_OCEAN:
                        System.out.println("Whoops, found a Lukewarm Ocean");
                        break;
                    case DEEP_LUKEWARM_OCEAN:
                        System.out.println("Whoops, found a Deep Lukewarm Ocean");
                        break;
                    case FROZEN_OCEAN:
                        System.out.println("Whoops, found a Frozen Ocean");
                        break;
                    case DEEP_FROZEN_OCEAN:
                        System.out.println("Whoops, found a Deep Frozen Ocean");
                        break;
                    case WARM_OCEAN:
                        System.out.println("Whoops, found a Warm Ocean");
                        break;
                    case RIVER:
                        System.out.println("Whoops, found a River");
                    default:
                        System.out.println("I don't know what happened");
                }
            }
            int deltaX = (int) (rand.nextDouble() * 5_000 + 5_000),
                    deltaZ = (int) (rand.nextDouble() * 5_000 + 5_000);
            if (rand.nextBoolean()) deltaX *= -1;
            if (rand.nextBoolean()) deltaZ *= -1;
            x += deltaX;
            z += deltaZ;
            newX = Math.round(deltaX);
            newZ = Math.round(deltaZ);
        }

        String newCoords = newX + " " + newZ;
        System.out.println("newCoords: " + newCoords);
        return newCoords;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        System.out.println("Respawning");
        Player p = event.getPlayer();
        String name = event.getPlayer().getName();
        System.out.println("Changing names for:" + name);
        scoreboard.getTeam(playerNames.get(p.getName())).addEntry(p.getName());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        System.out.println("Death has taken another victim");
        String name = event.getEntity().getName();
        System.out.println(name);
        String currTeam = playerNames.get(name);
        System.out.println("Player used to be: " + currTeam);
        switch (currTeam) {
            case GREEN:
                playerNames.put(name, YELLOW);
                break;
            case YELLOW:
                playerNames.put(name, RED);
                break;
            case RED:
                // Announce Death
                Bukkit.broadcastMessage("A RED HAS DIED! What a fool!");
                System.out.println("Changing Spawn");
                playerNames.put(name, GREEN);

                // Increment Death Count
                int index = playerCoords.getOrDefault(name,0);
                index += 1;
                playerCoords.put(name, index);

                // Find new Spawn
                if (usedCoords.size() == 0) {  // This shouldn't happen, since we should have already started the game
                    String coords = getNextCoords(0, 0);
                    usedCoords.add(coords);
                }
                while (usedCoords.size() <= index) {
                    String last = usedCoords.get(usedCoords.size()-1);
                    usedCoords.add(getNextCoords(Integer.parseInt(last.split(" ")[0]),Integer.parseInt(last.split(" ")[1])));
                }
                String coords = usedCoords.get(index);
                int x = Integer.parseInt(coords.split(" ")[0]);
                int y = 256;
                int z = Integer.parseInt(coords.split(" ")[1]);
                System.out.println("Setting Spawn");
                event.getEntity().getWorld().setSpawnLocation(x,y,z);
                event.getEntity().setBedSpawnLocation(event.getEntity().getWorld().getSpawnLocation());
                break;
            default:
                playerNames.put(name, GREEN);
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        System.out.println("Player has joined");
        String name = event.getPlayer().getName();
        playerNames.put(name, playerNames.getOrDefault(name, GREEN));
        scoreboard.getTeam(playerNames.get(name)).addEntry(name);
    }
    
}
