package lunadev.main.utils;

import lunadev.main.DDItems;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ConfigUtil {
    private final DDItems plugin;

    public ConfigUtil(DDItems plugin) {
        this.plugin = plugin;
    }

    public void reloadConfig() {
        plugin.reloadConfig();
    }

    public String getMessage(String key, Player player, String item, int uses, int time) {
        FileConfiguration config = plugin.getConfig();
        String message = config.getString("messages." + key, "&cСообщение не найдено!");

        if (message == null) return "";

        return HexColorUtil.colorize(message
                .replace("%player%", player.getName())
                .replace("%item%", item)
                .replace("%uses%", String.valueOf(uses))
                .replace("%time%", String.valueOf(time)));
    }
}
