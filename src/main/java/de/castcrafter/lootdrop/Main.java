package de.castcrafter.lootdrop;

import de.castcrafter.lootdrop.command.CommandManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The type Main.
 */
public class Main extends JavaPlugin {

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
	}

	@Override
	public void onEnable() {
		commandManager.registerCommands();
	}

	@Override
	public void onDisable() {
		commandManager.unregisterCommands();
	}
}
