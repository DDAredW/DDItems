package lunadev.main.commands;

import lunadev.main.DDItems;
import lunadev.main.managers.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DDItemsTabCompleter implements TabCompleter {
    private final ItemManager itemManager;

    public DDItemsTabCompleter(DDItems plugin) {
        this.itemManager = plugin.getItemManager();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (!sender.hasPermission("dditems.admin")) return completions;

        if (args.length == 1) {
            completions.add("reload");
            completions.add("give");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            completions.addAll(itemManager.getItems().keySet());
        } else if (args.length == 4 && args[0].equalsIgnoreCase("give")) {
            completions.add("1");
            completions.add("5");
            completions.add("10");
        }

        return completions;
    }
}
