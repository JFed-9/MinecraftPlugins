package JFed9.Generations;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class PlayerManager implements CommandExecutor, Listener {

    private Plugin plugin;
    
    private Map<String, ChatColor> playerNames;
    private List<String> usedCoords;
    private Map<String, Integer> playerCoords; 

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
        
        File playerStatsFile = new File("ps.gen"),
                usedCoordsFile = new File("uc.gen"),
                playerCoordsFile = new File("pc.gen");
        
        //  -------------- PREPARE PLAYER NAMES -------------
        if (playerStatsFile.exists()) {
            // Grab data from log file and initialize maps
            try {
                Scanner s = new Scanner(playerStatsFile);
                while (s.hasNextLine()) {
                    String line = s.nextLine();
                    String name = line.split(" ")[0],
                            color = line.split(" ")[1];
                    ChatColor chatColor;
                    switch (color) {
                        case "Green":
                            chatColor = ChatColor.GREEN;
                            break;
                        case "Yellow":
                            chatColor = ChatColor.YELLOW;
                            break;
                        case "Red":
                        default:
                            chatColor = ChatColor.RED;
                            break;
                    }
                    playerNames.put(name, chatColor);
                }
                s.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        // Go through online players and look for any new ones
        for (Player p : Bukkit.getOnlinePlayers())
            if (p != null && !playerNames.containsKey(p.getName()))
                playerNames.put(Objects.requireNonNull(p.getPlayer()).getName(), ChatColor.GREEN);

        //  -------------- PREPARE USED COORDS --------------
        if (usedCoordsFile.exists()) {
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
            try {
                Scanner s = new Scanner(playerStatsFile);
                while (s.hasNextLine()) {
                    String line = s.nextLine();
                    String name = line.split(" ")[0],
                            index = line.split(" ")[1];
                    playerCoords.put(name, Integer.valueOf(index));
                }
                s.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        // Grab all new players
        for (Player p : Bukkit.getOnlinePlayers())
            if (p != null && !playerCoords.containsKey(p.getName()))
                playerCoords.put(Objects.requireNonNull(p.getPlayer()).getName(), 0);
    }

    public void saveAll() throws IOException {
        PrintWriter prw = new PrintWriter("ps.gen");
        for (Map.Entry<String,ChatColor> entry : playerNames.entrySet()) {
            if (entry.getValue() == ChatColor.GREEN)
                prw.println(entry.getKey() + " " + "Green");
            else if (entry.getValue() == ChatColor.YELLOW)
                prw.println(entry.getKey() + " " + "Yellow");
            else if (entry.getValue() == ChatColor.RED)
                prw.println(entry.getKey() + " " + "Red");
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
        playerNames.replaceAll((k, v) -> ChatColor.GREEN);
        usedCoords = new LinkedList<>();
        usedCoords.add(getNextCoords(0,0));
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        String coords = usedCoords.get(0);
        for (Player p : Bukkit.getOnlinePlayers()) {
            System.out.println("Teleporting player");
            Block b = p.getWorld().getHighestBlockAt(Integer.parseInt(coords.split(" ")[0]), Integer.parseInt(coords.split(" ")[1]));
            p.teleport(b.getLocation());
            System.out.println("Changing ChatColor");
            p.setPlayerListName(ChatColor.GREEN + p.getName());
            p.setDisplayName(ChatColor.GREEN + p.getName());
        }
        return true;
    }
    
    private String getNextCoords(int x, int z) {
        Random rand = new Random();
        int deltaX = (int) (rand.nextDouble() * 5_000 + 5_000),
                deltaZ = (int) (rand.nextDouble() * 5_000 + 5_000);
        if (rand.nextBoolean()) deltaX *= -1;
        if (rand.nextBoolean()) deltaZ *= -1;
        x += deltaX;
        z += deltaZ;
        int newX = (int) Math.round(deltaX);
        int newZ = (int) Math.round(deltaZ);
        String newCoords = newX + " " + newZ;
        System.out.println("newCoords: " + newCoords);
        return newCoords;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        System.out.println("Respawn");
        Player p = event.getPlayer();
        String name = event.getPlayer().getName();
        System.out.println("Changing names");
        p.setPlayerListName(playerNames.get(name) + name);
        p.setDisplayName(playerNames.get(name) + name);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        System.out.println("Death");
        String name = event.getEntity().getName();
        System.out.println(name);
        ChatColor currChatColor = playerNames.get(name);
        System.out.println("Player: " + currChatColor);
        System.out.println("Green: " + ChatColor.GREEN);
        System.out.println("Yellow: " + ChatColor.YELLOW);
        System.out.println("Red: " + ChatColor.RED);
        if (currChatColor == ChatColor.GREEN)
            currChatColor = ChatColor.YELLOW;
        else if (currChatColor == ChatColor.YELLOW)
            currChatColor = ChatColor.RED;
        else if (currChatColor == ChatColor.RED) {
            System.out.println("Teleporting");
            currChatColor = ChatColor.GREEN;
            if (!playerCoords.containsKey(name))
                playerCoords.put(name,0);
            int index = playerCoords.get(name);
            index += 1;
            playerCoords.put(name, index);
            if (usedCoords.size() == 0)
                usedCoords.add(getNextCoords(0,0));
            while (usedCoords.size() <= index) {
                String last = usedCoords.get(usedCoords.size()-1);
                usedCoords.add(getNextCoords(Integer.parseInt(last.split(" ")[0]),Integer.parseInt(last.split(" ")[1])));
            }
            String coords = usedCoords.get(index);
            System.out.println("Finding highest block");
            Block b = event.getEntity().getWorld().getHighestBlockAt(Integer.parseInt(coords.split(" ")[0]), Integer.parseInt(coords.split(" ")[1]));
            System.out.println("Setting Spawn");
            event.getEntity().setBedSpawnLocation(b.getLocation());
        }
        else
            currChatColor = ChatColor.BLACK;
        playerNames.put(name, currChatColor);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        // I can't be bothered to implement this right now
    }
    
}
