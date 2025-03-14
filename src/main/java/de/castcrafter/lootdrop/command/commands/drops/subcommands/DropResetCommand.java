package de.castcrafter.lootdrop.command.commands.drops.subcommands;

import de.castcrafter.lootdrop.config.LootDropConfig;
import de.castcrafter.lootdrop.config.trades.SupplyTrade;
import de.castcrafter.lootdrop.utils.Chat;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.OfflinePlayerArgument;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.OfflinePlayer;

public class DropResetCommand extends CommandAPICommand {

  public DropResetCommand(String commandName) {
    super(commandName);

    withPermission("lootdrop.command.drops.reset");

    withArguments(new IntegerArgument("hour").replaceSuggestions(
        ArgumentSuggestions.stringCollection(info -> {
          LootDropConfig config = LootDropConfig.INSTANCE;

          return config.getTrades().stream().map(drop -> drop.getOffset() + "").toList();
        })));

    withOptionalArguments(new OfflinePlayerArgument("player"));

    executesPlayer((player, args) -> {
      Integer hourInteger = args.getUnchecked("hour");

      if (hourInteger == null) {
        throw CommandAPI.failWithString("Invalid hour");
      }

      long hour = hourInteger;
      Optional<OfflinePlayer> offlinePlayerOptional = args.getOptionalUnchecked("player");
      LootDropConfig config = LootDropConfig.INSTANCE;

      if (offlinePlayerOptional.isPresent()) {
        OfflinePlayer offlinePlayer = offlinePlayerOptional.get();

        config.getTrades(hour).forEach(trade -> trade.resetPlayer(offlinePlayer.getUniqueId()));

        Chat.sendMessage(
            player, Component.text(
                "Der Drop für " + offlinePlayer.getName() + " wurde zurückgesetzt",
                NamedTextColor.GREEN
            ));
      } else {
        config.getTrades().forEach(SupplyTrade::resetAllPlayers);
      }

      Chat.sendMessage(
          player, Component.text(
              "Alle Drops für die Stunde " + hour + " wurden zurückgesetzt",
              NamedTextColor.GREEN
          ));
    });
  }
}
