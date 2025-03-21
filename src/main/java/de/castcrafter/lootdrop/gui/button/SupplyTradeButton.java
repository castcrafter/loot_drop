package de.castcrafter.lootdrop.gui.button;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import de.castcrafter.lootdrop.config.LootDropConfig;
import de.castcrafter.lootdrop.config.trades.SupplyTrade;
import de.castcrafter.lootdrop.gui.drops.DropGui;
import de.castcrafter.lootdrop.gui.drops.DropsGui;
import de.castcrafter.lootdrop.utils.Chat;
import de.castcrafter.lootdrop.utils.SoundUtils;
import io.th0rgal.oraxen.api.OraxenItems;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SupplyTradeButton extends GuiItem {

  private static final ComponentLogger LOGGER = ComponentLogger.logger(SupplyTradeButton.class);

  private static ItemStack formItemStack(int index, Player forPlayer, SupplyTrade supplyTrade) {
    long secondsSinceStart = Duration.between(
        LootDropConfig.INSTANCE.getStartTimestamp(),
        ZonedDateTime.now()
    ).toSeconds();

    boolean playerOpenedDrop = !supplyTrade.canPlayerUse(forPlayer.getUniqueId());

    String oraxenName;
    if (secondsSinceStart < supplyTrade.getOffset()) {
      oraxenName = "icon_daily_gift_unclaimed";
    } else {
      oraxenName = "icon_daily_gift_ready";
    }

    if (playerOpenedDrop) {
      oraxenName = "icon_daily_gift_claimed";
    }

    ItemStack itemStack = OraxenItems.getItemById(oraxenName).build().clone();
    ItemMeta itemMeta = itemStack.getItemMeta();

    if (itemMeta == null) {
      LOGGER.error("ItemMeta is null for index {}", index);
      return itemStack;
    }

    itemMeta.displayName(
        Component.text("" + index,
            playerOpenedDrop ? NamedTextColor.RED : NamedTextColor.GREEN,
            TextDecoration.BOLD,
            TextDecoration.UNDERLINED
        ).decoration(TextDecoration.ITALIC, false));

    List<Component> loreList = new ArrayList<>();
    loreList.add(Component.empty());

    if (playerOpenedDrop) {
      loreList.add(Component.text("Du hast diesen Trade bereits geöffnet", NamedTextColor.RED));
    } else {
      loreList.add(
          Component.text("Klicke hier, um", NamedTextColor.GRAY));
      loreList.add(
          Component.text("die Belohnungen für Trade ", NamedTextColor.GRAY));
      loreList.add(
          Component.text(index + " zu öffnen", NamedTextColor.GRAY));
    }

    itemMeta.lore(
        loreList.stream().map(component -> component.decoration(TextDecoration.ITALIC, false))
            .toList());
    itemStack.setItemMeta(itemMeta);

    return itemStack;
  }

  public SupplyTradeButton(int index, DropsGui dropsGui, Player forPlayer,
      SupplyTrade supplyTrade) {
    super(formItemStack(index, forPlayer, supplyTrade), event -> {
          long currentSeconds = Duration.between(
              LootDropConfig.INSTANCE.getStartTimestamp(),
              ZonedDateTime.now()
          ).toSeconds();

          HumanEntity humanEntity = event.getWhoClicked();

          if (currentSeconds < supplyTrade.getOffset()) {
            Chat.sendMessage(humanEntity, Component.text(
                "Dieser Trade ist noch nicht verfügbar",
                NamedTextColor.RED
            ));

            SoundUtils.playSound(humanEntity, Sound.BLOCK_NOTE_BLOCK_BASS, .5f, 1f);

            return;
          }

          if (!supplyTrade.canPlayerUse(humanEntity.getUniqueId())) {
            Chat.sendMessage(
                humanEntity,
                Component.text("Du hast diesen Trade bereits geöffnet", NamedTextColor.RED));

            SoundUtils.playSound(humanEntity, Sound.BLOCK_NOTE_BLOCK_BASS, .5f, 1f);

            return;
          }

          new DropGui(dropsGui, humanEntity, supplyTrade).show(humanEntity);
        }
    );
  }
}
