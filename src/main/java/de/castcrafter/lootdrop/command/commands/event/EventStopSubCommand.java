package de.castcrafter.lootdrop.command.commands.event;

import de.castcrafter.lootdrop.Messages;
import de.castcrafter.lootdrop.command.commands.EventCommand;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Bukkit;

/**
 * The type Event start sub command.
 */
public class EventStopSubCommand extends CommandAPICommand {

	/**
	 * Instantiates a new Event start sub command.
	 *
	 * @param commandName  the command name
	 * @param eventCommand the event command
	 */
	public EventStopSubCommand(String commandName, final EventCommand eventCommand) {
		super(commandName);

		withPermission("castcrafter.event.stop");

		executes((sender, args) -> {
			if (!eventCommand.isEventStarted()) {
				sender.sendMessage(Messages.noEventStartedComponent());
				return;
			}

			eventCommand.setEventStarted(false);
			eventCommand.setEventLocation(null);

			Bukkit.broadcast(Messages.eventHasBeenStoppedComponent());
		});
	}
}
