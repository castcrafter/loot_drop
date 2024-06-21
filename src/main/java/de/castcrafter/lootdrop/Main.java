package de.castcrafter.lootdrop;

import de.castcrafter.lootdrop.command.CommandManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The type Main.
 */
public class Main extends JavaPlugin {

	private Loot_drop oldMain;

	private CommandManager commandManager;

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

		oldMain = new Loot_drop(this);
	}

	@Override
	public void onEnable() {
		commandManager.registerCommands();

		oldMain.onEnable();
	}

	@Override
	public void onDisable() {
		oldMain.onDisable();
		
		commandManager.unregisterCommands();
	}
}
