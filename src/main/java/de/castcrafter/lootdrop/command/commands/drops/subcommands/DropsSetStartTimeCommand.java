package de.castcrafter.lootdrop.command.commands.drops.subcommands;

import de.castcrafter.lootdrop.gui.drops.DropsGui;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * The type Drops set current time command.
 */
public class DropsSetStartTimeCommand extends CommandAPICommand {

	/**
	 * Instantiates a new Drops set current time command.
	 *
	 * @param commandName the command name
	 */
	public DropsSetStartTimeCommand(String commandName) {
		super(commandName);

		withPermission("lootdrop.command.drops.setstarttime");

		withArguments(new StringArgument("time"));

		executesPlayer((player, args) -> {
			String timeString = args.getUnchecked("time");

			if (timeString == null) {
				player.sendMessage("Bitte gib eine Zeit an.");
				return;
			}

			String[] time = timeString.split("-");
			int year = Integer.parseInt(time[ 0 ]);
			int month = Integer.parseInt(time[ 1 ]);
			int day = Integer.parseInt(time[ 2 ]);
			int hour = Integer.parseInt(time[ 3 ]);
			int minute = Integer.parseInt(time[ 4 ]);

			DropsGui.START_TIME = ZonedDateTime.of(
					year,
					month,
					day,
					hour,
					minute,
					0,
					0,
					ZoneId.of("Europe/Berlin")
			);

			player.sendMessage("§aDu hast die Startzeit auf §e" + DropsGui.START_TIME + " §agesetzt.");
		});
	}
}
