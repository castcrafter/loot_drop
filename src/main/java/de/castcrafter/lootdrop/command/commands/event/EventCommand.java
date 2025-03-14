package de.castcrafter.lootdrop.command.commands.event;

import de.castcrafter.lootdrop.command.commands.event.config.ConfigCommand;
import de.castcrafter.lootdrop.command.commands.event.duel.DuelCommand;
import de.castcrafter.lootdrop.command.commands.event.mines.MinesCommand;
import de.castcrafter.lootdrop.command.commands.event.subevent.SubEventCommand;
import dev.jorel.commandapi.CommandAPICommand;

public class EventCommand extends CommandAPICommand {

  public EventCommand(String commandName) {
    super(commandName);

    withPermission("castcrafter.event");

    withSubcommand(new MinesCommand("mines"));
    withSubcommand(new SubEventCommand("sub-event"));
    withSubcommand(new DuelCommand("duel"));
    withSubcommand(new ConfigCommand("config"));
  }
}
