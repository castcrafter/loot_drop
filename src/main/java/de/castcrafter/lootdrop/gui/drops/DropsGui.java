package de.castcrafter.lootdrop.gui.drops;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.component.PagingButtons;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import de.castcrafter.lootdrop.config.LootDropConfig;
import de.castcrafter.lootdrop.config.trades.SupplyTrade;
import de.castcrafter.lootdrop.gui.button.SupplyTradeButton;
import io.th0rgal.oraxen.api.OraxenItems;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DropsGui extends ChestGui {

  private final Player openedForPlayer;

  public DropsGui(Player openedForPlayer) {
    super(6, "<shift:-8><glyph:daily_rewards_ui>");

    this.openedForPlayer = openedForPlayer;

    setOnGlobalClick(event -> event.setCancelled(true));
    setOnGlobalDrag(event -> event.setCancelled(true));

    PaginatedPane paginatedPane = new PaginatedPane(1, 2, 7, 3);

    List<GuiItem> items = new ArrayList<>();
    List<SupplyTrade> trades = LootDropConfig.INSTANCE.getTrades();

    for (int i = 0; i < trades.size(); i++) {
      SupplyTrade trade = trades.get(i);
      items.add(getTradeGuiItem(i, trade));
    }

    paginatedPane.populateWithGuiItems(items);

    PagingButtons pagingButtons = new PagingButtons(Slot.fromXY(0, 3), 9, paginatedPane);
    setBackItem(pagingButtons);
    setForwardItem(pagingButtons);

    addPane(paginatedPane);
    addPane(pagingButtons);

    update();
  }

  public static void setBackItem(PagingButtons buttons) {
    ItemStack previous = OraxenItems.getItemById("daily_reward_previous").build();
    ItemMeta itemMeta = previous.getItemMeta();

    if (itemMeta != null) {
      itemMeta.displayName(Component.text("Zurück", NamedTextColor.GOLD).decoration(
          TextDecoration.ITALIC,
          false
      ));
      previous.setItemMeta(itemMeta);
    }

    buttons.setBackwardButton(
        new GuiItem(previous));
  }

  public static void setForwardItem(PagingButtons buttons) {
    ItemStack next = OraxenItems.getItemById("daily_reward_next").build();
    ItemMeta itemMeta = next.getItemMeta();

    if (itemMeta != null) {
      itemMeta.displayName(Component.text("Weiter", NamedTextColor.GOLD).decoration(
          TextDecoration.ITALIC,
          false
      ));
      next.setItemMeta(itemMeta);
    }

    buttons.setForwardButton(
        new GuiItem(next));
  }

  public GuiItem getTradeGuiItem(int index, SupplyTrade supplyTrade) {
    return new SupplyTradeButton(index, this, openedForPlayer, supplyTrade);
  }
}
