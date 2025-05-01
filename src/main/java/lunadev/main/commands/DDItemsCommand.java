package lunadev.main.commands;

import lunadev.main.DDItems;
import lunadev.main.managers.ItemManager;
import lunadev.main.utils.HexColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DDItemsCommand implements CommandExecutor {
    private final DDItems plugin;
    private final ItemManager itemManager;

    public DDItemsCommand(DDItems plugin) {
        this.plugin = plugin;
        this.itemManager = plugin.getItemManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("dditems.admin")) {
            sender.sendMessage(HexColorUtil.colorize(plugin.getConfig().getString("messages.no_permission")));
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            itemManager.loadItems();
            sender.sendMessage(HexColorUtil.colorize(plugin.getConfig().getString("messages.reload_success")));
            return true;
        }

        if (args.length == 4 && args[0].equalsIgnoreCase("give")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(HexColorUtil.colorize(plugin.getConfig().getString("messages.player_not_found")));
                return true;
            }

            String itemKey = args[2];
            int amount;
            try {
                amount = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                sender.sendMessage("§cВведите корректное число!");
                return true;
            }

            if (!itemManager.getItems().containsKey(itemKey)) {
                sender.sendMessage(HexColorUtil.colorize(plugin.getConfig().getString("messages.item_not_found")));
                return true;
            }

            for (int i = 0; i < amount; i++) {
                target.getInventory().addItem(itemManager.getItem(itemKey));
            }

            sender.sendMessage(HexColorUtil.colorize(plugin.getConfig().getString("messages.item_given")
                    .replace("%player%", target.getName())
                    .replace("%item%", itemKey)
                    .replace("%amount%", String.valueOf(amount))));
            return true;
        }

        sender.sendMessage("§cИспользование: /dditems <reload/give>");
        return true;
    }
}
