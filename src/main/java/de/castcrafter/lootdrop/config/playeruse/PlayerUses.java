package de.castcrafter.lootdrop.config.playeruse;

import java.util.List;
import java.util.UUID;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class PlayerUses {

  private String recipeName;
  private List<PlayerUse> uses;

  public PlayerUses() {
  }

  public PlayerUses(List<PlayerUse> uses, String recipeName) {
    this.uses = uses;
    this.recipeName = recipeName;
  }

  public boolean canPlayerUse(UUID player, int maxUses) {
    return uses.stream().filter(playerUse -> playerUse.getUuid().equals(player)).findFirst()
        .map(playerUse -> playerUse.getUses() < maxUses).orElse(true);
  }

  public void resetPlayer(UUID uuid) {
    uses.removeIf(playerUse -> playerUse.getUuid().equals(uuid));
  }

  public void resetAllPlayers() {
    uses.clear();
  }

  public void increasePlayerUses(UUID uuid) {
    uses.stream().filter(playerUse -> playerUse.getUuid().equals(uuid)).findFirst()
        .ifPresent(PlayerUse::increaseUses);
  }

  public int getPlayerUses(UUID uuid) {
    return uses.stream().filter(playerUse -> playerUse.getUuid().equals(uuid)).findFirst()
        .map(PlayerUse::getUses).orElse(0);
  }

  public String getRecipeName() {
    return recipeName;
  }

  public List<PlayerUse> getUses() {
    return uses;
  }
}
