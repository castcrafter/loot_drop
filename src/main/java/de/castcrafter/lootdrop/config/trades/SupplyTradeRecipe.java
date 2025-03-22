package de.castcrafter.lootdrop.config.trades;

import de.castcrafter.lootdrop.config.playeruse.PlayerUseConfig;
import de.castcrafter.lootdrop.config.playeruse.PlayerUses;
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

  private String name = null;

  private SupplyTradeItemStack firstItem = null;
  private SupplyTradeItemStack secondItem = null;
  private SupplyTradeItemStack resultItem = null;
  private String message = null;
  private String command = null;

  private int maxUses = 1;

  public SupplyTradeRecipe() {
  }

  public SupplyTradeRecipe(
      String name,
      SupplyTradeItemStack firstItem, SupplyTradeItemStack secondItem,
      SupplyTradeItemStack resultItem,
      int maxUses, Map<UUID, Integer> playerUses
  ) {
    this.name = name;
    this.firstItem = firstItem;
    this.secondItem = secondItem;
    this.resultItem = resultItem;
    this.maxUses = maxUses;
  }

  private PlayerUses getPlayerUses() {
    return PlayerUseConfig.INSTANCE.getUses(name);
  }

  public void resetPlayer(UUID uuid) {
    getPlayerUses().resetPlayer(uuid);
  }

  public void resetAllPlayers() {
    getPlayerUses().resetAllPlayers();
  }

  public boolean canPlayerUse(UUID uuid) {
    return getPlayerUses().canPlayerUse(uuid, maxUses);
  }

  public void increasePlayerUses(UUID uuid) {
    getPlayerUses().increasePlayerUses(uuid);
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
    if (message == null) {
      return null;
    }

    return MiniMessage.miniMessage().deserialize(message);
  }

  public String getCommand() {
    return command;
  }

  public MerchantRecipe toRecipe(UUID uuid) {
    MerchantRecipe merchantRecipe =
        new MerchantRecipe(resultItem.toItemStack(), getPlayerUses().getPlayerUses(uuid), maxUses,
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
        Objects.equals(resultItem, otherRecipe.resultItem);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstItem, secondItem, resultItem, maxUses);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
