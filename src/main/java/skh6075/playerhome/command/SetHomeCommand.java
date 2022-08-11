package skh6075.playerhome.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import skh6075.playerhome.PlayerHome;

public class SetHomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }

        Player player = (Player)sender;
        PlayerHome.getInstance().setHome(player, player.getLocation());
        player.sendMessage(PlayerHome.prefix + "홈을 지정했습니다. 홈으로 이동 하실때는 §f/홈 §7명령어를 사용해주세요.");
        return true;
    }
}
