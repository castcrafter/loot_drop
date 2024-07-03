package de.castcrafter.lootdrop.command.commands.drops.subcommands;

import de.castcrafter.lootdrop.config.LootDropConfig;
import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * The type Drops set default config command.
 */
public class DropsSetDefaultConfigCommand extends CommandAPICommand {

	/**
	 * Instantiates a new Drops set default config command.
	 *
	 * @param commandName the command name
	 */
	public DropsSetDefaultConfigCommand(String commandName) {
		super(commandName);

		withPermission("lootdrop.command.drops.setdefaultconfig");

		executesPlayer((player, args) -> {
			LootDropConfig config = LootDropConfig.INSTANCE;
			config.setDefaultConfig();
			config.saveConfig();

			player.sendMessage(Component.text("Default config set!", NamedTextColor.GREEN));
		});
	}
}
