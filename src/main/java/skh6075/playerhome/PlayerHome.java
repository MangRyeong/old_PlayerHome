package skh6075.playerhome;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import skh6075.playerhome.command.HomeCommand;
import skh6075.playerhome.command.SetHomeCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public final class PlayerHome extends JavaPlugin {
    public final static String prefix = "§l§6 [!]§r§7 ";

    private final static HashMap<String, Location> data = new HashMap<>();

    private static PlayerHome instance;

    public static PlayerHome getInstance(){
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        ArrayList<String> playerList = new ArrayList<>(getConfig().getKeys(false));
        if(playerList.isEmpty()){
            return;
        }

        for (String username : playerList) {
            String hash = getConfig().getString(username);
            if(hash == null){
                continue;
            }
            data.put(username, hashToPos(hash));
        }
    }

    @Override
    public void onDisable() {
        data.forEach((username, location) -> {
            getConfig().set(username, posToHash(location));
        });
        saveConfig();
    }

    public String posToHash(Location location){
        return location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getWorld().getName();
    }

    public Location hashToPos(String hash){
        String[] parse = hash.split(":");
        return new Location(
            getServer().getWorld(parse[3]),
            Double.parseDouble(parse[0]),
            Double.parseDouble(parse[1]),
            Double.parseDouble(parse[2]));
    }

    public void setHome(Player player, Location location){
        data.put(player.getName().toLowerCase(), location);
    }

    public boolean goHome(Player player){
        if(!data.containsKey(player.getName().toLowerCase())){
            player.sendMessage(prefix + "당신은 홈을 지정하지 않았습니다.");
            return false;
        }
        Location destination = data.get(player.getName().toLowerCase());
        player.teleport(destination);
        player.sendMessage(prefix + "홈으로 이동했습니다!");
        player.getWorld().playSound(destination, Sound.ENTITY_ENDERMAN_TELEPORT, 100.0F, 1.0F);
        return true;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }

        Player player = (Player)sender;
        if(command.getName().equalsIgnoreCase("셋홈")){
            setHome(player, player.getLocation());
            player.sendMessage(PlayerHome.prefix + "홈을 지정했습니다. 홈으로 이동 하실때는 §f/홈 §7명령어를 사용해주세요.");
        }else if(command.getName().equalsIgnoreCase("홈")){
            PlayerHome.getInstance().goHome(player);
        }
        return true;
    }
}
