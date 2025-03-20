package de.castcrafter.lootdrop.config.trades;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class SupplyTradeRecipe {

  private SupplyTradeItemStack firstItem = null;
  private SupplyTradeItemStack secondItem = null;
  private SupplyTradeItemStack resultItem = null;
  private String message = null;

  private int maxUses = 1;
  private Map<UUID, Integer> playerUses = new HashMap<>();

  public SupplyTradeRecipe() {
  }

  public SupplyTradeRecipe(
      SupplyTradeItemStack firstItem, SupplyTradeItemStack secondItem,
      SupplyTradeItemStack resultItem,
      int maxUses, Map<UUID, Integer> playerUses
  ) {
    this.firstItem = firstItem;
    this.secondItem = secondItem;
    this.resultItem = resultItem;
    this.playerUses = playerUses;
    this.maxUses = maxUses;
  }

  public void resetPlayer(UUID uuid) {
    playerUses.remove(uuid);
  }

  public void resetAllPlayers() {
    playerUses.clear();
  }

  public void increasePlayerUses(UUID uuid) {
    playerUses.put(uuid, playerUses.getOrDefault(uuid, 0) + 1);
  }

  public int getPlayerUses(UUID uuid) {
    return playerUses.getOrDefault(uuid, 0);
  }

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

  public void setPlayerUses(UUID uuid, int uses) {
    playerUses.put(uuid, uses);
  }

  public boolean canPlayerUse(UUID player) {
    return playerUses.getOrDefault(player, 0) < maxUses;
  }

  public SupplyTradeItemStack getFirstItem() {
    return firstItem;
  }

  public SupplyTradeItemStack getSecondItem() {
    return secondItem;
  }

  public SupplyTradeItemStack getResultItem() {
    return resultItem;
  }

  public Component getMessage() {
    return MiniMessage.miniMessage().deserialize(message);
  }

  public MerchantRecipe toRecipe(UUID uuid) {
    MerchantRecipe merchantRecipe =
        new MerchantRecipe(resultItem.toItemStack(), playerUses.getOrDefault(uuid, 0), maxUses,
            false);

    if (firstItem != null) {
      merchantRecipe.addIngredient(firstItem.toItemStack());
    } else {
      merchantRecipe.addIngredient(new ItemStack(Material.AIR));
    }

    if (secondItem != null) {
      merchantRecipe.addIngredient(secondItem.toItemStack());
    } else {
      merchantRecipe.addIngredient(new ItemStack(Material.AIR));
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

    SupplyTradeRecipe otherRecipe = (SupplyTradeRecipe) other;

    return maxUses == otherRecipe.maxUses && Objects.equals(firstItem, otherRecipe.firstItem) &&
        Objects.equals(secondItem, otherRecipe.secondItem) &&
        Objects.equals(resultItem, otherRecipe.resultItem) &&
        Objects.equals(playerUses, otherRecipe.playerUses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstItem, secondItem, resultItem, maxUses, playerUses);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
