package de.castcrafter.lootdrop.command.commands.duel;

import de.castcrafter.lootdrop.command.commands.duel.subcommands.DuelCreateCommand;
import dev.jorel.commandapi.CommandAPICommand;

/**
 * The type Duel command.
 */
public class DuelCommand extends CommandAPICommand {

	/**
	 * Instantiates a new Duel command.
	 *
	 * @param commandName the command name
	 */
	public DuelCommand(String commandName) {
		super(commandName);

		withPermission("castcrafter.lootdrop.duel");

		withSubcommand(new DuelCreateCommand("create"));
	}
}
