package de.castcrafter.lootdrop.gui.button;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import de.castcrafter.lootdrop.gui.drops.DropGui;
import de.castcrafter.lootdrop.gui.drops.DropsGui;
import de.castcrafter.lootdrop.utils.SoundUtils;
import io.th0rgal.oraxen.api.OraxenItems;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Drop hour button.
 */
public class DropHourButton extends GuiItem {

	private static final ComponentLogger LOGGER = ComponentLogger.logger(DropHourButton.class);

	/**
	 * Form item stack item stack.
	 *
	 * @param hour             the hour
	 * @param playerOpenedDrop the player opened drop
	 *
	 * @return the item stack
	 */
	private static ItemStack formItemStack(int hour, boolean playerOpenedDrop) {
		int currentHourSinceStart = DropsGui.getCurrentHourSinceStart(DropsGui.START_TIME, DropsGui.CURRENT_TIME);

		String oraxenName;
		if (currentHourSinceStart < hour) {
			oraxenName = "icon_daily_gift_unclaimed";
		} else {
			oraxenName = "icon_daily_gift_ready";
		}

		if (playerOpenedDrop) {
			oraxenName = "icon_daily_gift_claimed";
		}

		ItemStack itemStack = OraxenItems.getItemById(oraxenName).build().clone();
		itemStack.setAmount(hour <= itemStack.getMaxStackSize() ? hour : Math.min(hour, itemStack.getMaxStackSize()));

		ItemMeta itemMeta = itemStack.getItemMeta();

		if (itemMeta == null) {
			LOGGER.error("ItemMeta is null for hour " + hour);
			return itemStack;
		}

		itemMeta.displayName(
				Component.text("Stunde " + hour, playerOpenedDrop ? NamedTextColor.RED : NamedTextColor.GREEN,
							   TextDecoration.BOLD,
							   TextDecoration.UNDERLINED
				).decoration(TextDecoration.ITALIC, false));

		List<Component> loreList = new ArrayList<>();
		loreList.add(Component.empty());

		if (playerOpenedDrop) {
			loreList.add(Component.text("Du hast diese Stunde bereits geöffnet", NamedTextColor.RED));
		} else {
			loreList.add(
					Component.text("Klicke hier, um", NamedTextColor.GRAY));
			loreList.add(
					Component.text("die Belohnungen für", NamedTextColor.GRAY));
			loreList.add(
					Component.text("Stunde " + hour + " zu öffnen", NamedTextColor.GRAY));
		}

		itemMeta.lore(loreList.stream().map(component -> component.decoration(TextDecoration.ITALIC, false)).toList());
		itemStack.setItemMeta(itemMeta);

		return itemStack;
	}

	/**
	 * Instantiates a new Drop hour button.
	 *
	 * @param playerOpenedDrop the player opened drop
	 * @param hour             the hour
	 */
	public DropHourButton(boolean playerOpenedDrop, int hour) {
		super(formItemStack(hour, playerOpenedDrop), event -> {
				  int currentHour = DropsGui.getCurrentHourSinceStart(DropsGui.START_TIME, DropsGui.CURRENT_TIME);

				  HumanEntity humanEntity = event.getWhoClicked();

				  if (currentHour < hour) {
					  humanEntity.sendMessage(
							  Component.text("Diese Stunde ist noch nicht erreicht", NamedTextColor.RED));
					  SoundUtils.playSound(humanEntity, Sound.BLOCK_NOTE_BLOCK_BASS, .5f, 1f);
					  return;
				  }

				  if (humanEntity.getPersistentDataContainer().has(DropsGui.HOUR_DATA_KEY)) {
					  byte[] hourData = humanEntity.getPersistentDataContainer()
												   .get(DropsGui.HOUR_DATA_KEY, PersistentDataType.BYTE_ARRAY);
					  try {
						  byte data = DropsGui.getHourData(hour - 1, hourData);

						  if (data == 1) {
							  humanEntity.sendMessage(
									  Component.text(
											  "Du hast diese Stunde bereits geöffnet",
											  NamedTextColor.RED
									  ));

							  SoundUtils.playSound(humanEntity, Sound.BLOCK_NOTE_BLOCK_BASS, .5f, 1f);

							  return;
						  }
					  } catch (Exception exception) {
						  LOGGER.error(humanEntity.getName() + " tried to open hour " + hour + " gui but got an " +
									   "exception", exception);
					  }
				  } else {
					  humanEntity.getPersistentDataContainer()
								 .set(DropsGui.HOUR_DATA_KEY, PersistentDataType.BYTE_ARRAY,
									  DropsGui.getEmptyHourData(DropsGui.MAX_HOURS)
								 );
				  }

				  new DropGui(hour, DropGui.getRecipes()).show(humanEntity);
			  }
		);
	}
}
