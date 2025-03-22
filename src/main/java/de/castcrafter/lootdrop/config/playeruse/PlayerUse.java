package de.castcrafter.lootdrop.config.playeruse;

import java.util.UUID;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class PlayerUse {

  private UUID uuid;
  private int uses;

  public PlayerUse() {
  }

  public PlayerUse(UUID uuid, int uses) {
    this.uuid = uuid;
    this.uses = uses;
  }

  public void increaseUses() {
    uses++;
  }

  public UUID getUuid() {
    return uuid;
  }

  public int getUses() {
    return uses;
  }
}
