package who;

import com.andrei1058.bedwars.api.BedWars;
import com.andrei1058.bedwars.api.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

public class Main extends JavaPlugin {
    public static Main plugin;
    public static BedWars bw;
    public static final String PATH = "addons.who.";
    public static final String WHO_MESSAGE = PATH + "who-message-prefix";

    @Override
    public void onEnable() {
        plugin = this;
        if (Bukkit.getPluginManager().getPlugin("BedWars1058") == null) {
            getLogger().severe("BedWars1058 was not found. Disabling...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        org.bukkit.plugin.RegisteredServiceProvider<BedWars> registration = Bukkit.getServicesManager().getRegistration(BedWars.class);
        if (null == registration) {
            getLogger().severe("Cannot hook into BedWars1058.");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }
        bw = registration.getProvider();
        getLogger().info("Hooked into BedWars1058!");
        for (Language l : Language.getLanguages()) {
            if (!l.exists(WHO_MESSAGE)) {
                l.set(WHO_MESSAGE, "&b&lONLINE: ");
            }
        }
        this.getCommand("who").setExecutor(new WhoCommand());
    }

    static class WhoCommand implements CommandExecutor {
        @Override
        public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
            commandSender.sendMessage(Language.getMsg((Player)commandSender, WHO_MESSAGE) + Main.bw.getArenaUtil().getArenaByPlayer((Player)commandSender).getPlayers().stream().map(Player::getName).collect(Collectors.joining(", ")));
            return true;
        }
    }

}
