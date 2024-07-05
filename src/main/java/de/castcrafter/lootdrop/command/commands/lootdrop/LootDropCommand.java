package de.castcrafter.lootdrop.command.commands.lootdrop;

import de.castcrafter.lootdrop.command.commands.lootdrop.subcommands.LootDropCancelTimerCommand;
import de.castcrafter.lootdrop.command.commands.lootdrop.subcommands.LootDropReloadConfigCommand;
import de.castcrafter.lootdrop.command.commands.lootdrop.subcommands.LootDropStartTimerCommand;
import dev.jorel.commandapi.CommandAPICommand;

/**
 * The type Loot drop command.
 */
public class LootDropCommand extends CommandAPICommand {

	/**
	 * Instantiates a new Loot drop command.
	 *
	 * @param commandName the command name
	 */
	public LootDropCommand(String commandName) {
		super(commandName);

		withPermission("lootdrop.command.lootdrop");

		withSubcommand(new LootDropStartTimerCommand("start"));
		withSubcommand(new LootDropCancelTimerCommand("cancel"));
		withSubcommand(new LootDropReloadConfigCommand("reload-config"));
	}
}
