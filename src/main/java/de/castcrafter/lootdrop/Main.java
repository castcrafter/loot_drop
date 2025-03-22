package de.castcrafter.lootdrop;

import de.castcrafter.lootdrop.command.CommandManager;
import de.castcrafter.lootdrop.config.LootDropConfig;
import de.castcrafter.lootdrop.config.playeruse.PlayerUseConfig;
import de.castcrafter.lootdrop.listener.ListenerManager;
import de.castcrafter.lootdrop.placeholder.LootDropPlaceholderExpansion;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

  private Loot_drop oldMain;

  private CommandManager commandManager;
  private ListenerManager listenerManager;

  private SecureRandom random;

  public static Main getInstance() {
    return getPlugin(Main.class);
  }

  @Override
  public void onLoad() {
    try {
      random = SecureRandom.getInstanceStrong();
    } catch (NoSuchAlgorithmException e) {
      random = new SecureRandom();
    }

    LootDropConfig.INSTANCE.loadConfig();
    PlayerUseConfig.INSTANCE.loadConfig();

    commandManager = new CommandManager();
    listenerManager = new ListenerManager();

    oldMain = new Loot_drop(this);
  }

  @Override
  public void onEnable() {
    commandManager.registerCommands();
    listenerManager.registerListeners();

    oldMain.onEnable();

    LootDropConfig.INSTANCE.loadAndStartTimerIfExistsInConfig();

    new LootDropPlaceholderExpansion().register();
  }

  @Override
  public void onDisable() {
    oldMain.onDisable();
    commandManager.unregisterCommands();
    listenerManager.unregisterListeners();
  }

  public SecureRandom getRandom() {
    return random;
  }
}
