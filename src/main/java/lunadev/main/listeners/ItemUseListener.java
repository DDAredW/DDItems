package lunadev.main.listeners;

import lunadev.main.DDItems;
import lunadev.main.managers.ItemManager;
import lunadev.main.utils.HexColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemUseListener implements Listener {
    private final DDItems plugin;
    private final ItemManager itemManager;
    private final NamespacedKey itemUsesKey;
    private final NamespacedKey itemCooldownKey;
    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    public ItemUseListener(DDItems plugin) {
        this.plugin = plugin;
        this.itemManager = plugin.getItemManager();
        this.itemUsesKey = new NamespacedKey(plugin, "item_uses");
        this.itemCooldownKey = new NamespacedKey(plugin, "item_cooldown");
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        if (!event.getAction().toString().contains("RIGHT_CLICK")) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType().isAir() || !item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer data = meta.getPersistentDataContainer();
        if (!data.has(new NamespacedKey(plugin, "item_id"), PersistentDataType.STRING)) return;

        String key = data.get(new NamespacedKey(plugin, "item_id"), PersistentDataType.STRING);
        if (!itemManager.getItems().containsKey(key)) return;

        event.setCancelled(true);
        handleItemUse(player, key, item);
    }

    private void handleItemUse(Player player, String key, ItemStack item) {
        UUID playerId = player.getUniqueId();
        int cooldownTime = itemManager.getCooldown(key);

        cooldowns.putIfAbsent(playerId, new HashMap<>());
        long lastUse = cooldowns.get(playerId).getOrDefault(key, 0L);
        long timeLeft = (lastUse + (cooldownTime * 1000L)) - System.currentTimeMillis();

        if (timeLeft > 0) {
            player.sendMessage(HexColorUtil.colorize(plugin.getConfig().getString("messages.cooldown")
                    .replace("%time%", String.valueOf(timeLeft / 1000))));
            return;
        }

        cooldowns.get(playerId).put(key, System.currentTimeMillis());

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer data = meta.getPersistentDataContainer();
        int remainingUses = data.getOrDefault(itemUsesKey, PersistentDataType.INTEGER, 1);

        if (remainingUses <= 0) {
            player.sendMessage(HexColorUtil.colorize(plugin.getConfig().getString("messages.out_of_uses")
                    .replace("%item%", key)));
            player.getInventory().remove(item);
            return;
        }

        List<String> commands = plugin.getConfig().getStringList("items." + key + ".commands");
        for (String cmd : commands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
        }

        remainingUses--;
        data.set(itemUsesKey, PersistentDataType.INTEGER, remainingUses);

        List<String> loreList = plugin.getConfig().getStringList("items." + key + ".lore");
        int finalRemainingUses = remainingUses;
        List<String> updatedLore = loreList.stream()
                .map(line -> HexColorUtil.colorize(line.replace("%uses%", String.valueOf(finalRemainingUses))))
                .collect(Collectors.toList());

        meta.setLore(updatedLore);
        item.setItemMeta(meta);

        String soundUse = plugin.getConfig().getString("items." + key + ".sound_use");
        if (soundUse != null) {
            player.playSound(player.getLocation(), Sound.valueOf(soundUse), 1.0f, 1.0f);
        }

        if (remainingUses == 0) {
            player.getInventory().remove(item);
            player.sendMessage(HexColorUtil.colorize(plugin.getConfig().getString("messages.out_of_uses")
                    .replace("%item%", key)));

            String soundBreak = plugin.getConfig().getString("items." + key + ".sound_break");
            if (soundBreak != null) {
                player.playSound(player.getLocation(), Sound.valueOf(soundBreak), 1.0f, 1.0f);
            }
        }
    }
}
