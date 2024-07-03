package de.castcrafter.lootdrop.gui.drops;

import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.type.MerchantGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The type Drop gui.
 */
public class DropGui extends MerchantGui {

	private final List<MerchantRecipe> recipes;

	/**
	 * Gets recipes.
	 *
	 * @return the recipes
	 */
	public static List<MerchantRecipe> getRecipes() {
		List<MerchantRecipe> recipes = new ArrayList<>(); // FIXME: 02.07.2024 17:46 fetch from somewhere

		MerchantRecipe merchantRecipe = new MerchantRecipe(new ItemStack(Material.ACACIA_BOAT), 0, 1, false, 0, 1);
		merchantRecipe.setIngredients(
				List.of(new ItemStack(Material.ACACIA_DOOR), new ItemStack(Material.ACACIA_BUTTON)));

		recipes.add(merchantRecipe);

		return recipes;
	}

	/**
	 * Instantiates a new Drop gui.
	 *
	 * @param hour    the hour
	 * @param recipes the recipes
	 */
	public DropGui(int hour, List<MerchantRecipe> recipes) {
		super(ComponentHolder.of(Component.text("Stunde " + hour + " Drops")));

		this.recipes = recipes;

		recipes.forEach(this::addTrade);

		setOnClose(event -> {
			Inventory inventory = event.getInventory();

			if (!( inventory instanceof MerchantInventory merchantInventory )) {
				return;
			}

			ItemStack[] contents = merchantInventory.getContents();

			if (contents == null) {
				return;
			}

			HashMap<Integer, ItemStack> notAdded = event.getPlayer().getInventory().addItem(contents);
			if (notAdded.isEmpty()) {
				return;
			}

			notAdded.forEach(
					(index, item) -> event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), item));
		});

		setOnTopClick(event -> {
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

			if (!recipesContain(recipe)) {
				return;
			}
			PersistentDataContainer persistentDataContainer = event.getWhoClicked().getPersistentDataContainer();

			if (!persistentDataContainer.has(DropsGui.HOUR_DATA_KEY)) {
				persistentDataContainer.set(DropsGui.HOUR_DATA_KEY, PersistentDataType.BYTE_ARRAY,
											DropsGui.getEmptyHourData(DropsGui.MAX_HOURS)
				);
			}

			byte[] hourData =
					persistentDataContainer.getOrDefault(DropsGui.HOUR_DATA_KEY, PersistentDataType.BYTE_ARRAY,
														 DropsGui.getEmptyHourData(DropsGui.MAX_HOURS)
					);

			hourData[ hour - 1 ] = 1;

			persistentDataContainer.set(DropsGui.HOUR_DATA_KEY, PersistentDataType.BYTE_ARRAY, hourData);
		});
	}

	/**
	 * Recipes contain boolean.
	 *
	 * @param recipe the recipe
	 *
	 * @return the boolean
	 */
	private boolean recipesContain(MerchantRecipe recipe) {
		return recipes.stream().anyMatch(r -> recipeEquals(r, recipe));
	}

	/**
	 * Recipe equals boolean.
	 *
	 * @param recipeOne the recipe one
	 * @param recipeTwo the recipe two
	 *
	 * @return the boolean
	 */
	private boolean recipeEquals(MerchantRecipe recipeOne, MerchantRecipe recipeTwo) {
		return recipeOne.getResult().equals(recipeTwo.getResult()) &&
			   recipeOne.getUses() == recipeTwo.getUses() &&
			   recipeOne.getMaxUses() == recipeTwo.getMaxUses() &&
			   recipeOne.hasExperienceReward() == recipeTwo.hasExperienceReward() &&
			   recipeOne.getVillagerExperience() == recipeTwo.getVillagerExperience() &&
			   recipeOne.getPriceMultiplier() == recipeTwo.getPriceMultiplier() &&
			   recipeOne.getIngredients().equals(recipeTwo.getIngredients());
	}
}
