package JFed9.MinecraftMiniChallenges;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public interface MiniChallenge extends Listener {
    String getTaskDescription(int code);
    int getPointValue(int code);
    void registerListener(ChallengeCompleteListener listener);
    void deregisterListener(ChallengeCompleteListener listener);
    void onComplete(int code, Player player);
    interface ChallengeCompleteListener {
        void challengeCompleted(int code, Player player);
    }
}
