package de.castcrafter.lootdrop.config.drops;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * The type Hourly drop recipe.
 */
@ConfigSerializable
public class HourlyDropRecipe {

	private final int MAX_USES = 1;

	private HourlyDropItemStack firstItem = new HourlyDropItemStack("stone", 1, "Stone", List.of());
	private HourlyDropItemStack secondItem = new HourlyDropItemStack("stone", 1, "Stone", List.of());
	private HourlyDropItemStack resultItem = new HourlyDropItemStack("stone", 1, "Stone", List.of());

	private Map<UUID, Integer> playerUses = new HashMap<>();

	/**
	 * Instantiates a new Hourly drop recipe.
	 */
	public HourlyDropRecipe() {
	}

	/**
	 * Instantiates a new Hourly drop recipe.
	 *
	 * @param firstItem  the first item
	 * @param secondItem the second item
	 * @param resultItem the result item
	 * @param maxUses    the max uses
	 * @param playerUses the player uses
	 */
	public HourlyDropRecipe(
			HourlyDropItemStack firstItem, HourlyDropItemStack secondItem, HourlyDropItemStack resultItem,
			int maxUses, Map<UUID, Integer> playerUses
	) {
		this.firstItem = firstItem;
		this.secondItem = secondItem;
		this.resultItem = resultItem;
		this.playerUses = playerUses;
	}

	public int getPlayerUses(UUID uuid) {
		return playerUses.getOrDefault(uuid, 0);
	}

	/**
	 * Matches merchant recipe boolean.
	 *
	 * @param merchantRecipe the merchant recipe
	 *
	 * @return the boolean
	 */
	public boolean matchesMerchantRecipe(MerchantRecipe merchantRecipe) {
		boolean matches = true;

		List<ItemStack> ingredients = merchantRecipe.getIngredients();
		if (!ingredients.isEmpty()) {
			ItemStack first = ingredients.getFirst();
			ItemStack last = ingredients.getLast();

			if (firstItem != null && first != null) {
				matches &= firstItem.toItemStack().isSimilar(first);
			}

			if (secondItem != null && last != null) {
				matches &= secondItem.toItemStack().isSimilar(last);
			}
		}

		return matches;
	}

	/**
	 * Sets player uses.
	 *
	 * @param uuid the uuid
	 * @param uses the uses
	 */
	public void setPlayerUses(UUID uuid, int uses) {
		playerUses.put(uuid, uses);
	}

	/**
	 * Can player use boolean.
	 *
	 * @param player the player
	 *
	 * @return the boolean
	 */
	public boolean canPlayerUse(UUID player) {
		return playerUses.getOrDefault(player, 0) < MAX_USES;
	}

	/**
	 * Gets first item.
	 *
	 * @return the first item
	 */
	public HourlyDropItemStack getFirstItem() {
		return firstItem;
	}

	/**
	 * Gets second item.
	 *
	 * @return the second item
	 */
	public HourlyDropItemStack getSecondItem() {
		return secondItem;
	}

	/**
	 * Gets result item.
	 *
	 * @return the result item
	 */
	public HourlyDropItemStack getResultItem() {
		return resultItem;
	}

	/**
	 * To recipe merchant recipe.
	 *
	 * @param uuid the uuid
	 *
	 * @return the merchant recipe
	 */
	public MerchantRecipe toRecipe(UUID uuid) {
		MerchantRecipe merchantRecipe =
				new MerchantRecipe(resultItem.toItemStack(), playerUses.getOrDefault(uuid, 0), MAX_USES, false);

		if (firstItem != null) {
			merchantRecipe.addIngredient(firstItem.toItemStack());
		}

		if (secondItem != null) {
			merchantRecipe.addIngredient(secondItem.toItemStack());
		}

		return merchantRecipe;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}

		if (other == null || getClass() != other.getClass()) {
			return false;
		}

		HourlyDropRecipe otherRecipe = (HourlyDropRecipe) other;

		return MAX_USES == otherRecipe.MAX_USES && Objects.equals(firstItem, otherRecipe.firstItem) &&
			   Objects.equals(secondItem, otherRecipe.secondItem) &&
			   Objects.equals(resultItem, otherRecipe.resultItem) &&
			   Objects.equals(playerUses, otherRecipe.playerUses);
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstItem, secondItem, resultItem, MAX_USES, playerUses);
	}
}
