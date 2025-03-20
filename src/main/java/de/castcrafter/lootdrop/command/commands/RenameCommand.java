package de.castcrafter.lootdrop.command.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RenameCommand extends CommandAPICommand {

  public RenameCommand(String commandName) {
    super(commandName);

    withPermission("lootdrop.command.rename");
    withArguments(new GreedyStringArgument("name"));

    executesPlayer((player, args) -> {
      String name = args.getUnchecked("name");

      if (name == null || name.isBlank()) {
        player.sendMessage(Component.text("Du musst einen Namen angeben.", NamedTextColor.RED));
        return;
      }

      ItemStack item = player.getInventory().getItemInMainHand();

      if (item.getType().isAir()) {
        player.sendMessage(
            Component.text("Du musst ein Item in der Hand halten.", NamedTextColor.RED));
        return;
      }

      ItemMeta meta = item.getItemMeta();
      meta.displayName(
          MiniMessage.miniMessage().deserialize(name).decoration(TextDecoration.ITALIC, false));

      item.setItemMeta(meta);

      player.sendMessage(
          Component.text("Du hast den Namen erfolgreich geändert.", NamedTextColor.GREEN));
    });
  }
}
