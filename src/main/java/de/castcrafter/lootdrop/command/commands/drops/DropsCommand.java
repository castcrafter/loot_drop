package de.castcrafter.lootdrop.command.commands.drops;

import de.castcrafter.lootdrop.command.commands.drops.subcommands.DropReloadCommand;
import de.castcrafter.lootdrop.command.commands.drops.subcommands.DropResetCommand;
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

    withSubcommand(new DropResetCommand("reset"));
    withSubcommand(new DropReloadCommand("reload"));

    executesPlayer((player, args) -> {
//			new DropsGui(player).show(player);
    });
  }
}
