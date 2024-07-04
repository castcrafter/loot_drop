package de.castcrafter.lootdrop.command.commands.drops.subcommands;

import de.castcrafter.lootdrop.config.LootDropConfig;
import de.castcrafter.lootdrop.gui.drops.DropsGui;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

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

			if (hour < 0) {
				player.sendMessage(Component.text("Die Stunde muss >= 0 sein.", NamedTextColor.RED));

				return;
			}

			DropsGui.CURRENT_TIME = LootDropConfig.INSTANCE.getStartedTimestamp().plusHours(hour);

			player.sendMessage(Component.text("Du hast die Stunde auf ", NamedTextColor.GREEN)
										.append(Component.text(hour, NamedTextColor.YELLOW))
										.append(Component.text(" gesetzt.", NamedTextColor.GREEN)));
		});
	}
}
