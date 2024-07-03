package de.castcrafter.lootdrop.gui.drops;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.component.PagingButtons;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import de.castcrafter.lootdrop.gui.button.DropHourButton;
import io.th0rgal.oraxen.api.OraxenItems;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Drops gui.
 */
public class DropsGui extends ChestGui {

	public static final int MAX_HOURS = 24;
	public static final NamespacedKey HOUR_DATA_KEY = new NamespacedKey("lootdrop", "hour_data");

	public static ZonedDateTime START_TIME = ZonedDateTime.of(
			2024,
			7,
			2,
			18,
			0,
			0,
			0,
			ZoneId.of("Europe/Berlin")
	);

	public static ZonedDateTime CURRENT_TIME = ZonedDateTime.of(
			2024,
			7,
			2,
			18,
			0,
			0,
			0,
			ZoneId.of("Europe/Berlin")
	);

	private final Player openedForPlayer;

	/**
	 * Gets hour data.
	 *
	 * @param hour     the hour
	 * @param hourData the hour data
	 *
	 * @return the hour data
	 */
	public static byte getHourData(int hour, byte[] hourData) {
		if (hour < 0 || hour >= hourData.length) {
			throw new IllegalArgumentException("Hour out of bounds");
		}

		return hourData[ hour ];
	}

	/**
	 * Get empty hour data byte [ ].
	 *
	 * @param maxHours the max hours
	 *
	 * @return the byte [ ]
	 */
	public static byte[] getEmptyHourData(int maxHours) {
		return new byte[ maxHours ];
	}

	/**
	 * Gets current hour since start.
	 *
	 * @param startTime   the start time
	 * @param currentTime the current time
	 *
	 * @return the current hour since start
	 */
	public static int getCurrentHourSinceStart(ZonedDateTime startTime, ZonedDateTime currentTime) {
		return (int) ( ( currentTime.toEpochSecond() - startTime.toEpochSecond() ) / 3600 );
	}

	/**
	 * Instantiates a new Drops gui.
	 *
	 * @param openedForPlayer the opened for player
	 */
	public DropsGui(Player openedForPlayer) {
		super(6, "<shift:-8><glyph:daily_rewards_ui>");

		this.openedForPlayer = openedForPlayer;

		setOnGlobalClick(event -> event.setCancelled(true));
		setOnGlobalDrag(event -> event.setCancelled(true));

		PaginatedPane paginatedPane = new PaginatedPane(1, 2, 7, 3);

		List<GuiItem> items = new ArrayList<>();
		for (int i = 0; i < MAX_HOURS; i++) {
			items.add(getHourGuiItem(i + 1));
		}
		paginatedPane.populateWithGuiItems(items);

		PagingButtons pagingButtons = new PagingButtons(Slot.fromXY(0, 3), 9, paginatedPane);
		setBackItem(pagingButtons);
		setForwardItem(pagingButtons);

		addPane(paginatedPane);
		addPane(pagingButtons);

		update();
	}

	/**
	 * Sets back item.
	 *
	 * @param buttons the buttons
	 */
	public void setBackItem(PagingButtons buttons) {
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

	/**
	 * Sets forward item.
	 *
	 * @param buttons the buttons
	 */
	public void setForwardItem(PagingButtons buttons) {
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

	/**
	 * Create items pane paginated pane.
	 *
	 * @param hour the hour
	 *
	 * @return the paginated pane
	 */
	public GuiItem getHourGuiItem(int hour) {
		PersistentDataContainer container = openedForPlayer.getPersistentDataContainer();
		boolean openedByPlayer = container.has(HOUR_DATA_KEY) && getHourData(hour - 1, container.get(
				HOUR_DATA_KEY,
				PersistentDataType.BYTE_ARRAY
		)) == 1;

		return new DropHourButton(openedByPlayer, hour);
	}
}
