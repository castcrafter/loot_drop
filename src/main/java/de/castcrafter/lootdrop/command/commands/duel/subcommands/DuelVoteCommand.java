package de.castcrafter.lootdrop.command.commands.duel.subcommands;

import de.castcrafter.lootdrop.duel.Duel;
import de.castcrafter.lootdrop.duel.DuelGui;
import de.castcrafter.lootdrop.duel.DuelManager;
import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * The type Duel vote command.
 */
public class DuelVoteCommand extends CommandAPICommand {

	/**
	 * Instantiates a new Duel vote command.
	 *
	 * @param commandName the command name
	 */
	public DuelVoteCommand(String commandName) {
		super(commandName);

		withPermission("castcrafter.lootdrop.duel.vote");

		executesPlayer((player, args) -> {
			Duel duel = DuelManager.INSTANCE.getRunningDuel();

			if (duel == null) {
				player.sendMessage(Component.text(
						"Es läuft aktuell kein Duell, für das du abstimmen kannst!",
						NamedTextColor.RED
				));

				return;
			}

			if (!duel.isVoteOpen()) {
				player.sendMessage(Component.text(
						"Die Abstimmung für dieses Duell ist bereits beendet!",
						NamedTextColor.RED
				));

				return;
			}

			new DuelGui(duel).show(player);
		});
	}
}
