package JFed9.MinecraftMiniChallenges;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddPlayerExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length==0) return false;
        Player player = Bukkit.getPlayer(strings[0]);
        if (player != null) ScoreBoard.getInstance().givePoints(player,0);
        else return false;
        return true;
    }
}
