package JFed9.MinecraftMiniChallenges;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public abstract class ChallengeWithListener implements MiniChallenge {
    private List<ChallengeCompleteListener> listeners;

    @Override
    public void registerListener(ChallengeCompleteListener listener) {
        if (listeners == null) listeners = new ArrayList<>();
        listeners.add(listener);
    }

    @Override
    public void deregisterListener(ChallengeCompleteListener listener) {
        if (listeners == null) listeners = new ArrayList<>();
        listeners.remove(listener);
    }

    @Override
    public void onComplete(int code, Player player) {
        if (listeners == null) return;
        for (ChallengeCompleteListener listener : listeners) listener.challengeCompleted(code, player);
    }
}
