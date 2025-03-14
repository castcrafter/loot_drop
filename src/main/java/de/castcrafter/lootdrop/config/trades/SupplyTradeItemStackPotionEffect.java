package de.castcrafter.lootdrop.config.trades;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
@SuppressWarnings("FieldMayBeFinal")
public class SupplyTradeItemStackPotionEffect {

  private static final ComponentLogger LOGGER = ComponentLogger.logger(
      SupplyTradeItemStackPotionEffect.class);

  private String potionEffectNamespace = null;
  private String potionEffect;
  private int duration;
  private int amplifier;
  private boolean ambient = false;
  private boolean particles = false;
  private boolean icon = true;

  public SupplyTradeItemStackPotionEffect() {
  }

  public SupplyTradeItemStackPotionEffect(
      String potionEffect, int duration, int amplifier, boolean ambient, boolean particles,
      boolean icon
  ) {
    this.potionEffect = potionEffect;
    this.duration = duration;
    this.amplifier = amplifier;
    this.ambient = ambient;
    this.particles = particles;
    this.icon = icon;
  }

  public PotionEffect getPotionEffect() {
    return new PotionEffect(getPotionEffectType(), duration, amplifier, ambient, particles, icon);
  }

  public PotionEffectType getPotionEffectType() {
    String namespace = potionEffectNamespace;

    if (namespace == null) {
      namespace = "minecraft";
    }

    return Registry.POTION_EFFECT_TYPE.get(new NamespacedKey(namespace, potionEffect));
  }

  public int getDuration() {
    return duration;
  }

  public int getAmplifier() {
    return amplifier;
  }

  public boolean isAmbient() {
    return ambient;
  }

  public boolean isParticles() {
    return particles;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
