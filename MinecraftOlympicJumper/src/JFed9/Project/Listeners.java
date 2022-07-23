package JFed9.Project;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Listeners implements Listener {


    @EventHandler
    public void onJump(PlayerMoveEvent e) {
        if (!e.getPlayer().isOnGround()) {

        }
    }
}
