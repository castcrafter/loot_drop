package de.castcrafter.lootdrop.config.trades;

import de.castcrafter.lootdrop.config.LootDropConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class SupplyTrade {

  private String name = null;
  private long offset = 0;
  private List<SupplyTradeRecipe> recipes = new ArrayList<>();

  public SupplyTrade() {
  }

  public SupplyTrade(String name, long offset, List<SupplyTradeRecipe> recipes) {
    this.name = name;
    this.offset = offset;
    this.recipes = recipes;
  }

  public static List<SupplyTrade> getByOffset(long offset) {
    return LootDropConfig.INSTANCE.getTrades().stream().filter(drop -> drop.getOffset() == offset)
        .toList();
  }

  public void resetPlayer(UUID uuid) {
    recipes.forEach(recipe -> recipe.resetPlayer(uuid));
  }

  public void resetAllPlayers() {
    recipes.forEach(SupplyTradeRecipe::resetAllPlayers);
  }

  public boolean canPlayerUse(UUID uuid) {
    return recipes.stream().anyMatch(recipe -> recipe.canPlayerUse(uuid));
  }

  public long getOffset() {
    return offset;
  }

  public List<SupplyTradeRecipe> getRecipes() {
    return recipes;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (other == null || getClass() != other.getClass()) {
      return false;
    }

    SupplyTrade otherDrop = (SupplyTrade) other;

    return offset == otherDrop.offset && Objects.equals(recipes, otherDrop.recipes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(offset, recipes);
  }
}
