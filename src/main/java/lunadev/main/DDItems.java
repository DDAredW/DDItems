package lunadev.main;

import lunadev.main.commands.DDItemsCommand;
import lunadev.main.commands.DDItemsTabCompleter;
import lunadev.main.listeners.ItemUseListener;
import lunadev.main.managers.ItemManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DDItems extends JavaPlugin {
    private static DDItems instance;
    private ItemManager itemManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        itemManager = new ItemManager(this);
        getCommand("dditems").setExecutor(new DDItemsCommand(this));
        getCommand("dditems").setTabCompleter(new DDItemsTabCompleter(this));
        getServer().getPluginManager().registerEvents(new ItemUseListener(this), this);
        getLogger().info("DDItems успешно загружен!");
    }

    @Override
    public void onDisable() {
        getLogger().info("DDItems отключен!");
    }

    public static DDItems getInstance() {
        return instance;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }
}
