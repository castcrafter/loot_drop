package de.castcrafter.lootdrop.command.commands.drops.subcommands;

import de.castcrafter.lootdrop.gui.drops.DropsGui;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;

/**
 * The type Drops set current hour command.
 */
public class DropsSetCurrentHourCommand extends CommandAPICommand {

	/**
	 * Instantiates a new Drops set current hour command.
	 *
	 * @param commandName the command name
	 */
	public DropsSetCurrentHourCommand(String commandName) {
		super(commandName);

		withPermission("lootdrop.command.drops.setcurrenthour");

		withArguments(new IntegerArgument("hour"));

		executesPlayer((player, args) -> {
			int hour = args.getOrDefaultUnchecked("hour", -1);

			if (hour < 0 || hour >= 48) {
				player.sendMessage("Die Stunde muss zwischen 0 und 47 liegen.");
				return;
			}

			DropsGui.CURRENT_TIME = DropsGui.START_TIME.plusHours(hour);
			player.sendMessage("§aDu hast die Stunde auf §e" + hour + " §agesetzt.");
		});
	}
}
