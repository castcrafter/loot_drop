package de.castcrafter.lootdrop.command.commands.lootdrop.subcommands;

import de.castcrafter.lootdrop.config.LootDropConfig;
import de.castcrafter.lootdrop.timer.LootDropTimer;
import de.castcrafter.lootdrop.utils.Chat;
import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * The type Loot drop pause timer command.
 */
public class LootDropCancelTimerCommand extends CommandAPICommand {

	/**
	 * Instantiates a new Loot drop cancel timer command.
	 *
	 * @param commandName the command name
	 */
	public LootDropCancelTimerCommand(String commandName) {
		super(commandName);

		withPermission("lootdrop.command.lootdrop.cancel");

		executesPlayer((player, args) -> {
			LootDropTimer timer = LootDropConfig.INSTANCE.getTimer();

			if (timer == null || !timer.isRunning()) {
				Chat.sendMessage(player, Component.text("Es läuft kein Timer!", NamedTextColor.RED));
				return;
			}

			timer.stop();
			Chat.sendMessage(player, Component.text("Timer wurde abgebrochen!", NamedTextColor.GREEN));
		});
	}
}
