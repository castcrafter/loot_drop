package de.castcrafter.lootdrop.config.trades;

import java.util.Objects;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class SupplyTradeItemStackEnchantment {

  private String enchantmentNamespace = null;
  private String enchantment;
  private int level = 1;

  public SupplyTradeItemStackEnchantment() {
  }

  public SupplyTradeItemStackEnchantment(String enchantmentNamespace, String enchantment,
      int level) {
    this.enchantmentNamespace = enchantmentNamespace;
    this.enchantment = enchantment;
    this.level = level;
  }

  public Enchantment getEnchantment() {
    String namespace = enchantmentNamespace;

    if (namespace == null) {
      namespace = "minecraft";
    }

    return Registry.ENCHANTMENT.get(new NamespacedKey(namespace, enchantment));
  }

  public String getEnchantmentString() {
    return enchantment;
  }

  public String getEnchantmentNamespace() {
    return enchantmentNamespace;
  }

  public int getLevel() {
    return level;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }

    SupplyTradeItemStackEnchantment otherEnchantment = (SupplyTradeItemStackEnchantment) other;

    return level == otherEnchantment.level &&
        Objects.equals(enchantmentNamespace, otherEnchantment.enchantmentNamespace) &&
        Objects.equals(enchantment, otherEnchantment.enchantment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enchantmentNamespace, enchantment, level);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
