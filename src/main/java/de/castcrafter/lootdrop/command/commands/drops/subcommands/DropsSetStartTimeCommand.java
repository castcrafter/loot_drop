package de.castcrafter.lootdrop.command.commands.drops.subcommands;

import de.castcrafter.lootdrop.config.LootDropConfig;
import de.castcrafter.lootdrop.utils.Chat;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

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

		withArguments(new IntegerArgument("year"));
		withArguments(new IntegerArgument("month"));
		withArguments(new IntegerArgument("day"));
		withArguments(new IntegerArgument("hour"));
		withArguments(new IntegerArgument("minute"));

		executesPlayer((player, args) -> {
			int year = args.getOrDefaultUnchecked("year", 2024);
			int month = args.getOrDefaultUnchecked("month", 1);
			int day = args.getOrDefaultUnchecked("day", 1);
			int hour = args.getOrDefaultUnchecked("hour", 0);
			int minute = args.getOrDefaultUnchecked("minute", 0);

			ZonedDateTime newTimestamp = ZonedDateTime.of(
					year,
					month,
					day,
					hour,
					minute,
					0,
					0,
					ZoneId.of("Europe/Berlin")
			);

			LootDropConfig.INSTANCE.setStartedTimestamp(newTimestamp);
			LootDropConfig.INSTANCE.saveConfig();

			Chat.sendMessage(player, Component.text("Die Startzeit wurde auf ", NamedTextColor.GREEN)
											  .append(Component.text(newTimestamp.toString(), NamedTextColor.YELLOW))
											  .append(Component.text(" gesetzt.", NamedTextColor.GREEN)));
		});
	}
}
