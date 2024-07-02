package de.castcrafter.lootdrop.gui.drops;

import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import de.castcrafter.lootdrop.gui.button.DropHourButton;
import de.castcrafter.lootdrop.utils.ItemUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * The type Drops gui.
 */
public class DropsGui extends ChestGui {

	public static final int MAX_HOURS = 23;
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
		super(5, ComponentHolder.of(Component.text("Drops")));

		this.openedForPlayer = openedForPlayer;

		setOnGlobalClick(event -> event.setCancelled(true));
		setOnGlobalDrag(event -> event.setCancelled(true));

		OutlinePane topOutlinePane = new OutlinePane(0, 0, 9, 1);
		OutlinePane bottomOutlinePane = new OutlinePane(0, 4, 9, 1);

		topOutlinePane.addItem(new GuiItem(ItemUtils.getItemStack(Material.BLACK_STAINED_GLASS_PANE, 1, 0,
																  Component.text(" ")
		)));
		topOutlinePane.setRepeat(true);

		bottomOutlinePane.addItem(new GuiItem(ItemUtils.getItemStack(Material.BLACK_STAINED_GLASS_PANE, 1, 0,
																	 Component.text(" ")
		)));
		bottomOutlinePane.setRepeat(true);

		StaticPane staticPane = new StaticPane(0, 1, 9, 3);

		int hour = 1;
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				if (( y == 2 && ( x == 0 || x == 1 ) ) || ( y == 2 && ( x == 7 || x == 8 ) )) {
					continue;
				}

				staticPane.addItem(getHourGuiItem(hour), x, y);
				hour++;

				System.out.println(hour);
			}
		}

		addPane(staticPane);
		addPane(topOutlinePane);
		addPane(bottomOutlinePane);

		update();
	}

	/**
	 * Create items pane paginated pane.
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
