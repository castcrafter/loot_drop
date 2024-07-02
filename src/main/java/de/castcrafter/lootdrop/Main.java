package de.castcrafter.lootdrop;

import de.castcrafter.lootdrop.command.CommandManager;
import de.castcrafter.lootdrop.listener.ListenerManager;
import de.castcrafter.lootdrop.placeholder.LootDropPlaceholderExpansion;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The type Main.
 */
public class Main extends JavaPlugin {

	private Loot_drop oldMain;

	private CommandManager commandManager;
	private ListenerManager listenerManager;

	/**
	 * Gets instance.
	 *
	 * @return the instance
	 */
	public static Main getInstance() {
		return getPlugin(Main.class);
	}

	@Override
	public void onLoad() {
		commandManager = new CommandManager();
		listenerManager = new ListenerManager();

		oldMain = new Loot_drop(this);
	}

	@Override
	public void onEnable() {
		commandManager.registerCommands();
		listenerManager.registerListeners();

		oldMain.onEnable();
		new LootDropPlaceholderExpansion().register();
	}

	@Override
	public void onDisable() {
		oldMain.onDisable();

		commandManager.unregisterCommands();
		listenerManager.unregisterListeners();
	}
}
