package de.castcrafter.lootdrop.command.commands.lootdrop.subcommands;

import de.castcrafter.lootdrop.config.LootDropConfig;
import de.castcrafter.lootdrop.utils.Chat;
import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * The type Loot drop reload config command.
 */
public class LootDropReloadConfigCommand extends CommandAPICommand {
	
	/**
	 * Instantiates a new Loot drop reload config command.
	 *
	 * @param commandName the command name
	 */
	public LootDropReloadConfigCommand(String commandName) {
		super(commandName);

		withPermission("lootdrop.command.lootdrop.reloadconfig");

		executesPlayer((player, args) -> {
			LootDropConfig config = LootDropConfig.INSTANCE;
			config.loadConfig();

			Chat.sendMessage(player, Component.text("Config reloaded!", NamedTextColor.GREEN));
		});
	}
}
