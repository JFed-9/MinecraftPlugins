package JFed9.MinecraftMiniChallenges;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
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
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.*;

import java.util.*;
import java.util.function.Function;

import static JFed9.MinecraftMiniChallenges.ChallengeCodes.*;

public class ScoreBoard implements MiniChallenge.ChallengeCompleteListener {
    private final Map<Player,Integer> scores = new HashMap<>();
    private List<Integer> challenges;
    private final Map<Integer,String> flags = new HashMap<>();

    private int current_task;
    private Listeners listeners;

    private Plugin plugin;
    private Objective objective;

    private Random rand;

    private static ScoreBoard instance;
    public static ScoreBoard getInstance() {
        if (instance == null) instance = new ScoreBoard();
        return instance;
    }

    public void initialize(Plugin plugin) {
        this.plugin = plugin;
        rand = new Random(new Date().getTime());
        rand.setSeed(new Date().getTime());
        listeners = new Listeners();
        listeners.registerListener(this);

        challenges = new ArrayList<>();

        //Trivial
        challenges.add(EAT_STEAK);
        //Easy
        challenges.add(FIND_DIAMONDS);
        challenges.add(NETHER_ENTER);
        challenges.add(REACH_256);
        challenges.add(NETHER_ROOF);
        challenges.add(BUILD_SNOWMAN);
        challenges.add(DIORITE_BREAK);
        challenges.add(GET_COPPER_BLOCK);
        //Medium
        challenges.add(ENCHANT);
        challenges.add(KILL_BLAZE);
        challenges.add(KILL_GHAST);
        challenges.add(MUSIC_DISK_GET);
        challenges.add(KILL_WITHER);
        challenges.add(ENTER_GEODE);
        challenges.add(GOAT_MASSACRE);
        challenges.add(AXOLOTL_CAPTURE);
        //Hard
        challenges.add(ENDER_EYE_GET);
        challenges.add(ANCIENT_DEBRIS);
        challenges.add(GHAST_CAPTURE);
        challenges.add(END_ENTER);

        ScoreboardManager manager = plugin.getServer().getScoreboardManager();
        if (manager == null) return;
        Scoreboard b = manager.getMainScoreboard();
        if (b.getObjectives().stream().anyMatch(item -> item.getName().equals("MiniScores")))
            objective = b.getObjective("MiniScores");
        if (objective != null) objective.unregister();
        objective = b.registerNewObjective("MiniScores", "dummy", ChatColor.GOLD + "Mini Challenges");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

    }

    public void newTask() {
        removeTask();
        if (challenges.size()==0) gameOver();
        else {
            current_task = challenges.get(rand.nextInt(challenges.size()));
            listeners.getPrepWork(current_task).apply(null);
            setScoreboardText();
            //Start timer
        }
    }

    @Override
    public void challengeCompleted(int code, Player player) {
        if (code == current_task) {
            plugin.getServer().broadcastMessage(player.getDisplayName() + " completed the challenge!");
            givePoints(player, listeners.getPointValue(code));
            newTask();
        }
    }

    public void startGames() {
        plugin.getServer().getPluginManager().registerEvents(listeners,plugin);
        newTask();
    }

    public void givePoints(Player player, int points) {
        if (scores.containsKey(player)) scores.put(player,scores.get(player) + points);
        else scores.put(player,points);
    }

    private void setScoreboardText() {
        for (Player player : scores.keySet()) {
            Score score = objective.getScore(ChatColor.WHITE + player.getDisplayName());
            score.setScore(scores.get(player));
        }
        objective.getScore(ChatColor.AQUA + "TASK: " + ChatColor.DARK_GREEN + listeners.getTaskDescription(current_task)).setScore(-1);
    }

    private void removeTask() {
        Scoreboard scoreboard = objective.getScoreboard();
        if (scoreboard != null) scoreboard.resetScores(ChatColor.AQUA + "TASK: " + ChatColor.DARK_GREEN + listeners.getTaskDescription(current_task));
        challenges.remove((Integer) current_task);
    }

    public void gameOver() {
        plugin.getServer().broadcastMessage("Game over!");
        if (scores.size() == 0) return;
        Player winner = Collections.max(scores.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
        plugin.getServer().broadcastMessage(winner.getDisplayName() + " won the match!");
        removeTask();
        current_task = 0;
        setScoreboardText();
        deregister();
    }

    private void deregister() {
        CraftItemEvent.getHandlerList().unregister(listeners);
        BlockBreakEvent.getHandlerList().unregister(listeners);
        EnchantItemEvent.getHandlerList().unregister(listeners);
        EntityDeathEvent.getHandlerList().unregister(listeners);
        PlayerMoveEvent.getHandlerList().unregister(listeners);
        InventoryCloseEvent.getHandlerList().unregister(listeners);
        VehicleEnterEvent.getHandlerList().unregister(listeners);
        CreatureSpawnEvent.getHandlerList().unregister(listeners);
        PlayerItemConsumeEvent.getHandlerList().unregister(listeners);
        PlayerBucketFillEvent.getHandlerList().unregister(listeners);
    }

    public void setFlag(int flag, String data) {
        flags.put(flag,data);
    }

    public String getFlag(int flag) {
        if (flags.containsKey(flag)) return flags.get(flag);
        return "";
    }

}