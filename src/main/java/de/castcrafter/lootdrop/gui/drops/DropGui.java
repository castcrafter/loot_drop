package de.castcrafter.lootdrop.gui.drops;

import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.type.MerchantGui;
import de.castcrafter.lootdrop.config.drops.HourlyDrop;
import de.castcrafter.lootdrop.config.drops.HourlyDropRecipe;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * The type Drop gui.
 */
public class DropGui extends MerchantGui {
	/**
	 * Instantiates a new Drop gui.
	 *
	 * @param openedForPlayer the opened for player
	 * @param hourlyDrop      the hourly drop
	 */
	public DropGui(HumanEntity openedForPlayer, HourlyDrop hourlyDrop) {
		super(ComponentHolder.of(Component.text("Stunde " + hourlyDrop.getHour() + " Drops")));

		List<MerchantRecipe> recipes = hourlyDrop.getMerchantRecipes(openedForPlayer.getUniqueId());
		recipes.forEach(this::addTrade);

		setOnClose(event -> {
			Inventory inventory = event.getInventory();

			if (!( inventory instanceof MerchantInventory merchantInventory )) {
				return;
			}

			List<ItemStack> contents = Arrays.asList(merchantInventory.getContents());

			for (ItemStack content : contents) {
				if (content == null || content.getType().equals(Material.AIR)) {
					continue;
				}

				HashMap<Integer, ItemStack> notAdded = event.getPlayer().getInventory().addItem(content);

				if (notAdded.isEmpty()) {
					continue;
				}

				notAdded.forEach(
						(index, item) -> event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), item));
			}
		});

		setOnTopClick(event -> {
			InventoryAction[] allowedActions = new InventoryAction[] {
					InventoryAction.PICKUP_ALL,
			};

			if (Arrays.stream(allowedActions).noneMatch(action -> action.equals(event.getAction()))) {
				event.setCancelled(true);
				return;
			}

			ItemStack currentItem = event.getCurrentItem();

			if (currentItem == null) {
				return;
			}

			if (!( event.getClickedInventory() != null &&
				   event.getClickedInventory() instanceof MerchantInventory merchantInventory )) {
				return;
			}

			MerchantRecipe recipe = merchantInventory.getSelectedRecipe();

			if (recipe == null || !currentItem.equals(recipe.getResult())) {
				return;
			}

			HourlyDropRecipe dropRecipe = hourlyDrop.findByMerchantRecipe(recipe);

			if (dropRecipe == null) {
				return;
			}

			HumanEntity humanEntity = event.getWhoClicked();

			int currentPlayerUses = dropRecipe.getPlayerUses(humanEntity.getUniqueId());
			int clickedItemAmount = currentItem.getAmount();
			int resultAmount = recipe.getResult().getAmount();

			int newUses = currentPlayerUses + clickedItemAmount / resultAmount;

			recipe.setUses(newUses);
			dropRecipe.setPlayerUses(humanEntity.getUniqueId(), newUses);
		});
	}
}
