package de.castcrafter.lootdrop.command.commands.drops.subcommands;

import de.castcrafter.lootdrop.config.LootDropConfig;
import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class DropsReloadConfigCommand extends CommandAPICommand {
	public DropsReloadConfigCommand(String commandName) {
		super(commandName);

		withPermission("lootdrop.command.drops.reloadconfig");

		executesPlayer((player, args) -> {
			LootDropConfig config = LootDropConfig.INSTANCE;
			config.loadConfig();

			player.sendMessage(Component.text("Config reloaded!", NamedTextColor.GREEN));
		});
	}
}
