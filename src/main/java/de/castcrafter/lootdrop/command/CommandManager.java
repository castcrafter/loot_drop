package de.castcrafter.lootdrop.command;

import de.castcrafter.lootdrop.command.commands.EventCommand;
import de.castcrafter.lootdrop.command.commands.EventJoinCommand;
import de.castcrafter.lootdrop.command.commands.seamine.SeamineCommand;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Command manager.
 */
public class CommandManager {

	private final List<CommandAPICommand> commands;

	/**
	 * Instantiates a new Command manager.
	 */
	public CommandManager() {
		this.commands = new ArrayList<>();

		this.commands.add(EventCommand.INSTANCE);
		this.commands.add(new EventJoinCommand("join"));
		this.commands.add(new SeamineCommand("seamine"));
	}

	/**
	 * Register commands.
	 */
	public void registerCommands() {
		commands.forEach(CommandAPICommand::register);
	}

	/**
	 * Unregister commands.
	 */
	public void unregisterCommands() {
		commands.stream().map(CommandAPICommand::getName).forEach(CommandAPI::unregister);
	}

	/**
	 * Gets commands.
	 *
	 * @return the commands
	 */
	public List<CommandAPICommand> getCommands() {
		return commands;
	}
}
