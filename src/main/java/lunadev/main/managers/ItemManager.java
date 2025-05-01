package lunadev.main.managers;

import lunadev.main.DDItems;
import lunadev.main.utils.HexColorUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class ItemManager {
    private final DDItems plugin;
    private final Map<String, ItemStack> items = new HashMap<>();
    private final Map<String, Integer> cooldowns = new HashMap<>();

    public ItemManager(DDItems plugin) {
        this.plugin = plugin;
        loadItems();
    }

    public void loadItems() {
        items.clear();
        cooldowns.clear();

        FileConfiguration config = plugin.getConfig();
        ConfigurationSection itemsSection = config.getConfigurationSection("items");
        if (itemsSection == null) return;

        for (String key : itemsSection.getKeys(false)) {
            ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
            if (itemSection == null) continue;

            String name = HexColorUtil.colorize(itemSection.getString("name", key));
            Material material = Material.valueOf(itemSection.getString("material", "STONE").toUpperCase());
            int itemUses = itemSection.getInt("uses", 1);
            int itemCooldown = itemSection.getInt("cooldown", 0);

            List<String> loreList = itemSection.getStringList("lore");
            List<String> lore = new ArrayList<>();
            for (String line : loreList) {
                lore.add(HexColorUtil.colorize(line.replace("%uses%", String.valueOf(itemUses))));
            }

            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(name);
                meta.setLore(lore);
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);

                int customModelData = new Random().nextInt(999999) + 1;
                meta.setCustomModelData(customModelData);

                PersistentDataContainer data = meta.getPersistentDataContainer();
                data.set(new NamespacedKey(plugin, "item_id"), PersistentDataType.STRING, key);
                data.set(new NamespacedKey(plugin, "item_uses"), PersistentDataType.INTEGER, itemUses);

                item.setItemMeta(meta);
            }

            items.put(key, item);
            cooldowns.put(key, itemCooldown);
        }
    }

    public ItemStack getItem(String key) {
        ItemStack original = items.get(key);
        if (original == null) return null;

        ItemStack newItem = original.clone();
        newItem.setAmount(1);

        ItemMeta meta = newItem.getItemMeta();
        if (meta != null) {
            meta.setCustomModelData(new Random().nextInt(999999) + 1);

            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(new NamespacedKey(plugin, "item_uses"), PersistentDataType.INTEGER, getUses(key));

            newItem.setItemMeta(meta);
        }

        return newItem;
    }

    public int getCooldown(String key) {
        return cooldowns.getOrDefault(key, 0);
    }

    public int getUses(String key) {
        return items.containsKey(key) ? items.get(key).getItemMeta().getPersistentDataContainer()
                .getOrDefault(new NamespacedKey(plugin, "item_uses"), PersistentDataType.INTEGER, 1) : 1;
    }

    public Map<String, ItemStack> getItems() {
        return items;
    }
}
