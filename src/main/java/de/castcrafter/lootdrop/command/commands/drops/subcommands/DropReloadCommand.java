package de.castcrafter.lootdrop.command.commands.drops.subcommands;

import de.castcrafter.lootdrop.config.LootDropConfig;
import de.castcrafter.lootdrop.utils.Chat;
import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class DropReloadCommand extends CommandAPICommand {

    public DropReloadCommand(String commandName) {
        super(commandName);

        withPermission("lootdrop.command.drops.reset");

        withSubcommand(new CommandAPICommand("confirm")
                .withPermission("lootdrop.command.drops.reset")
                .executesPlayer((player, args) -> {
                    LootDropConfig config = LootDropConfig.INSTANCE;
                    config.loadConfig();

                    Chat.sendMessage(
                            player, Component.text("Drops configuration reloaded!",
                                    NamedTextColor.GREEN
                            ));
                }));

        executesPlayer((player, args) -> {
            Chat.sendMessage(player, Component.text(
                    "Are you sure you want to reload the drops configuration? This will reset all current player progress. To save progress, run /save-all first!",
                    NamedTextColor.RED
            ));
            Chat.sendMessage(player, Component.text(
                    "Use /drops reload confirm to reload the configuration!",
                    NamedTextColor.RED
            ));
        });
    }
}