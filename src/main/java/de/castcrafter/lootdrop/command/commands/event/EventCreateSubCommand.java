package de.castcrafter.lootdrop.command.commands.event;

import de.castcrafter.lootdrop.Messages;
import de.castcrafter.lootdrop.command.commands.EventCommand;
import de.castcrafter.lootdrop.utils.Chat;
import dev.jorel.commandapi.CommandAPICommand;

/**
 * The type Event create sub command.
 */
public class EventCreateSubCommand extends CommandAPICommand {

	/**
	 * Instantiates a new Event create sub command.
	 *
	 * @param commandName  the command name
	 * @param eventCommand the event command
	 */
	public EventCreateSubCommand(String commandName, final EventCommand eventCommand) {
		super(commandName);

		withPermission("castcrafter.event.create");

		executesPlayer((player, args) -> {
			eventCommand.setEventLocation(player.getLocation());

			Chat.sendMessage(player, Messages.eventHasBeenCreatedComponent());
		});
	}
}
