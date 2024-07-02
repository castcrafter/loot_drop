package de.castcrafter.lootdrop.command.commands.duel.subcommands;

import de.castcrafter.lootdrop.duel.Duel;
import de.castcrafter.lootdrop.duel.DuelManager;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.TimeArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * The type Duel create command.
 */
public class DuelCreateCommand extends CommandAPICommand {

	/**
	 * Instantiates a new Duel create command.
	 *
	 * @param commandName the command name
	 */
	public DuelCreateCommand(String commandName) {
		super(commandName);

		withPermission("castcrafter.lootdrop.duel.create");

		withArguments(new PlayerArgument("playerOne"));
		withArguments(new PlayerArgument("playerTwo"));

		withArguments(new TimeArgument("voteDuration"));

		executesPlayer((player, args) -> {
			if (DuelManager.INSTANCE.getRunningDuel() != null) {
				player.sendMessage(Component.text(
						"Es läuft bereits ein Duell. Bitte beende dieses, bevor du ein " +
						"neues starten kannst!", NamedTextColor.RED
				));

				return;
			}

			Player playerOne = args.getUnchecked("playerOne");
			Player playerTwo = args.getUnchecked("playerTwo");

			int voteDuration = args.getOrDefaultUnchecked("voteDuration", 0) / 20;
			List<ItemStack> rewards =
					Arrays.stream(player.getInventory().getStorageContents())
						  .filter(itemStack -> itemStack != null && !itemStack.getType().equals(
								  Material.AIR)).toList();
			
			Duel runningDuel = new Duel(playerOne, playerTwo, rewards, voteDuration);
			DuelManager.INSTANCE.setRunningDuel(runningDuel);

			runningDuel.openDuelGui(true);
		});
	}
}
