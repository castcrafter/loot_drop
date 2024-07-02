package de.castcrafter.lootdrop.command.commands.drops;

import de.castcrafter.lootdrop.command.commands.drops.subcommands.DropsSetCurrentHourCommand;
import de.castcrafter.lootdrop.command.commands.drops.subcommands.DropsSetStartTimeCommand;
import de.castcrafter.lootdrop.gui.drops.DropsGui;
import dev.jorel.commandapi.CommandAPICommand;

/**
 * The type Drops command.
 */
public class DropsCommand extends CommandAPICommand {

	/**
	 * Instantiates a new Drops command.
	 *
	 * @param commandName the command name
	 */
	public DropsCommand(String commandName) {
		super(commandName);

		withPermission("lootdrop.command.drops");

		withSubcommand(new DropsSetCurrentHourCommand("setcurrenthour"));
		withSubcommand(new DropsSetStartTimeCommand("setcurrenttime"));

		executesPlayer((player, args) -> {
			new DropsGui(player).show(player);
		});
	}
}
