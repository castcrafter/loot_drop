package de.castcrafter.lootdrop.command.commands.event;

import de.castcrafter.lootdrop.Messages;
import de.castcrafter.lootdrop.command.commands.EventCommand;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * The type Event start sub command.
 */
public class EventStartSubCommand extends CommandAPICommand {

	/**
	 * Instantiates a new Event start sub command.
	 *
	 * @param commandName  the command name
	 * @param eventCommand the event command
	 */
	public EventStartSubCommand(String commandName, final EventCommand eventCommand) {
		super(commandName);

		withPermission("castcrafter.event.start");

		executes((sender, args) -> {
			if (eventCommand.getEventLocation() == null) {
				sender.sendMessage(Messages.noEventCreatedComponent());
				return;
			}

			eventCommand.setEventStarted(true);

			Bukkit.broadcast(Messages.eventHasBeenStartedComponent());

			for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
				onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);
			}
		});
	}
}
