package de.castcrafter.lootdrop.command.commands;

import dev.jorel.commandapi.CommandAPICommand;
import java.time.Duration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class LiegeWieCommand extends CommandAPICommand {

  public LiegeWieCommand(String commandName) {
    super(commandName);

    withPermission("lootdrop.command.liegewie");

    executesPlayer((player, args) -> {

      Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
        onlinePlayer.getInventory().addItem(new ItemStack(Material.LIME_BED));
        onlinePlayer.showTitle(
            Title.title(Component.text("Liege wie", NamedTextColor.GREEN), Component.text(""),
                Times.times(
                    Duration.ofMillis(250), Duration.ofSeconds(5), Duration.ofMillis(250))));
      });
    });
  }
}
