package de.castcrafter.lootdrop.command.commands;

import de.castcrafter.lootdrop.command.commands.event.EventCreateSubCommand;
import de.castcrafter.lootdrop.command.commands.event.EventStartSubCommand;
import de.castcrafter.lootdrop.command.commands.event.EventStopSubCommand;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Location;

/**
 * The type Event command.
 */
public class EventCommand extends CommandAPICommand {

	public static final EventCommand INSTANCE = new EventCommand("event");

	private boolean eventStarted;
	private Location eventLocation;

	/**
	 * Instantiates a new Event command.
	 *
	 * @param commandName the command name
	 */
	public EventCommand(String commandName) {
		super(commandName);

		withPermission("castcrafter.event");

		withSubcommand(new EventStartSubCommand("start", this));
		withSubcommand(new EventStopSubCommand("stop", this));
		withSubcommand(new EventCreateSubCommand("create", this));
	}

	/**
	 * Gets event location.
	 *
	 * @return the event location
	 */
	public Location getEventLocation() {
		return eventLocation;
	}

	/**
	 * Is event started boolean.
	 *
	 * @return the boolean
	 */
	public boolean isEventStarted() {
		return eventStarted;
	}

	/**
	 * Sets event location.
	 *
	 * @param eventLocation the event location
	 */
	public void setEventLocation(Location eventLocation) {
		this.eventLocation = eventLocation;
	}

	/**
	 * Sets event started.
	 *
	 * @param eventStarted the event started
	 */
	public void setEventStarted(boolean eventStarted) {
		this.eventStarted = eventStarted;
	}
}
