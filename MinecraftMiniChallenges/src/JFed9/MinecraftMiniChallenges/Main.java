package JFed9.MinecraftMiniChallenges;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("MiniChallenges Enabled");
        ScoreBoard.getInstance().initialize(this);

        PluginCommand command;
        command = getCommand("addPlayer");
        if (command != null) command.setExecutor(new AddPlayerExecutor());
        command = getCommand("startMiniChallenges");
        if (command != null) command.setExecutor(new StartChallengesExecutor());
        command = getCommand("gameOver");
        if (command != null) command.setExecutor(new GameOverExecutor());
        command = getCommand("skip");
        if (command != null) command.setExecutor(new SkipExecutor());

        //NOTE:
        /*
        When adding challenges, you must:
        - Create a unique code in "ChallengeCodes.java"
        - Add that code to the challenges list in "ScoreBoard.java"
        - Write the event listener in "Listeners.java"
        - Assign a description and point value in "Listeners.java"
        - Add any new Event types to the deregister function in "ScoreBoard.java"
         */
    }

    @Override
    public void onDisable() {
        getLogger().info("MiniChallenges Disabled");
    }
}
