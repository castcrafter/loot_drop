package de.castcrafter.lootdrop.listener;

import de.castcrafter.lootdrop.Main;
import de.castcrafter.lootdrop.larry.LarryListener;
import de.castcrafter.lootdrop.listener.listeners.ChestListener;
import de.castcrafter.lootdrop.listener.listeners.ConfigSaveListener;
import de.castcrafter.lootdrop.listener.listeners.JoinListener;
import de.castcrafter.lootdrop.listener.listeners.SeamineListener;
import de.castcrafter.lootdrop.listener.listeners.SpecialItemListener;
import de.castcrafter.lootdrop.locator.LocatorListener;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The type Listener manager.
 */
public class ListenerManager {

  /**
   * Register listeners.
   */
  public void registerListeners() {
    JavaPlugin plugin = Main.getInstance();
    PluginManager pluginManager = Bukkit.getPluginManager();

    pluginManager.registerEvents(new SeamineListener(), plugin);
    pluginManager.registerEvents(new SpecialItemListener(), plugin);
    pluginManager.registerEvents(new ConfigSaveListener(), plugin);
    pluginManager.registerEvents(new LocatorListener(), plugin);
    pluginManager.registerEvents(new LarryListener(), plugin);
    pluginManager.registerEvents(new ChestListener(), plugin);
    pluginManager.registerEvents(new JoinListener(), plugin);
  }

  /**
   * Unregister listeners.
   */
  public void unregisterListeners() {
    HandlerList.unregisterAll(Main.getInstance());
  }

}
