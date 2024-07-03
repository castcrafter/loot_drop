package de.castcrafter.lootdrop.config.drops;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The type Hourly drop item stack.
 */
@ConfigSerializable
public class HourlyDropItemStack {

	public static final LegacyComponentSerializer SECTION_SERIALIZER =
			LegacyComponentSerializer.builder().extractUrls().hexColors().character('§').build();

	public static final LegacyComponentSerializer AND_SERIALIZER =
			LegacyComponentSerializer.builder().extractUrls().hexColors().character('&').build();

	private String material = "STONE";
	private int amount = 1;
	private String displayName = null;
	private List<String> lore = null;

	/**
	 * Instantiates a new Hourly drop item stack.
	 */
	public HourlyDropItemStack() {
	}

	/**
	 * Instantiates a new Hourly drop item stack.
	 *
	 * @param material    the material
	 * @param amount      the amount
	 * @param displayName the display name
	 * @param lore        the lore
	 */
	public HourlyDropItemStack(String material, int amount, String displayName, List<String> lore) {
		this.material = material;
		this.amount = amount;
		this.displayName = displayName;
		this.lore = lore;
	}

	/**
	 * Gets material.
	 *
	 * @return the material
	 */
	public String getMaterialName() {
		return material;
	}

	/**
	 * Gets amount.
	 *
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Gets display name.
	 *
	 * @return the display name
	 */
	public final Component getDisplayNameComponent() {
		return SECTION_SERIALIZER.deserialize(AND_SERIALIZER.serialize(AND_SERIALIZER.deserialize(displayName)));
	}

	/**
	 * Gets lore.
	 *
	 * @return the lore
	 */
	public final List<Component> getLoreComponentList() {
		List<Component> loreList = new ArrayList<>();

		for (String loreLine : lore) {
			loreList.add(
					SECTION_SERIALIZER.deserialize(AND_SERIALIZER.serialize(AND_SERIALIZER.deserialize(loreLine))));
		}

		return loreList;
	}

	/**
	 * To item stack item stack.
	 *
	 * @return the item stack
	 */
	public ItemStack toItemStack() {
		ItemStack itemStack = new ItemStack(Material.valueOf(material.toUpperCase()), amount);
		ItemMeta itemMeta = itemStack.getItemMeta();

		if (displayName != null) {
			itemMeta.displayName(getDisplayNameComponent().decoration(TextDecoration.ITALIC, false));
		}

		if (lore != null && !lore.isEmpty()) {
			itemMeta.lore(getLoreComponentList().stream().map(component -> component.decoration(
					TextDecoration.ITALIC,
					false
			)).toList());
		}

		itemStack.setItemMeta(itemMeta);

		return itemStack;
	}

	/**
	 * Gets display name.
	 *
	 * @return the display name
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Gets lore.
	 *
	 * @return the lore
	 */
	public List<String> getLore() {
		return lore;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}

		if (other == null || getClass() != other.getClass()) {
			return false;
		}

		HourlyDropItemStack otherItemStack = (HourlyDropItemStack) other;

		return amount == otherItemStack.amount && Objects.equals(material, otherItemStack.material) &&
			   Objects.equals(displayName, otherItemStack.displayName) &&
			   Objects.equals(lore, otherItemStack.lore);
	}

	@Override
	public int hashCode() {
		return Objects.hash(material, amount, displayName, lore);
	}
}
