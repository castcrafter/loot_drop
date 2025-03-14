package de.castcrafter.lootdrop.locator;

import de.castcrafter.lootdrop.larry.LarryNpc;
import it.unimi.dsi.fastutil.Pair;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class LocatorListener implements Listener {

  @EventHandler
  public void onMove(PlayerMoveEvent event) {
    Player player = event.getPlayer();
    Location startingLocation = player.getEyeLocation();

    Location toLocate = LarryNpc.locateNpc();
    if (toLocate == null) {
      return;
    }

    Pair<LocatorResult, LocatorResult> result = Locators.getLocator(Location.class)
        .map(locator -> locator.locate(startingLocation, toLocate))
        .orElse(Pair.of(LocatorResult.NONE, LocatorResult.NONE));

    LocatorResult direction = result.left();
    LocatorResult height = result.right();

    String directionDisplay = Locators.LOCATOR_RESULT_DISPLAY.get(direction);
    String heightDisplay = Locators.LOCATOR_RESULT_DISPLAY.get(height);

    TextColor color = TextColor.fromHexString("#00FF40");

    TextComponent.Builder actionBar = Component.text();
    actionBar.append(Component.text("ғɪɴᴅᴇ ʟᴀʀʀʏ", color, TextDecoration.BOLD));
    actionBar.append(Component.text(" | ", NamedTextColor.GRAY));
    actionBar.append(Component.text(directionDisplay, color));
    actionBar.append(Component.text(" | ", NamedTextColor.GRAY));
    actionBar.append(Component.text(heightDisplay, color));

    player.sendActionBar(actionBar.build());
  }

}