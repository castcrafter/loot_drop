package de.castcrafter.lootdrop.config.trades;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.DyedItemColor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@SuppressWarnings("UnstableApiUsage")
@ConfigSerializable
public class SupplyTradeItemStack {

  private static final ComponentLogger LOGGER = ComponentLogger.logger();

  private String material = "STONE";
  private int amount = 1;
  private String displayName = null;
  private List<String> lore = null;
  private boolean unbreakable = false;
  private float customModelData = -1;
  private String leatherColor = null;

  private List<SupplyTradeItemStackEnchantment> enchantments = null;

  private List<SupplyTradeItemStackPotionEffect> potionEffects = null;
  private String potionColor = null;

  public SupplyTradeItemStack() {
  }

  public SupplyTradeItemStack(
      String material, int amount, String displayName, List<String> lore,
      List<SupplyTradeItemStackEnchantment> enchantments,
      boolean unbreakable,
      String potionColor,
      List<SupplyTradeItemStackPotionEffect> potionEffects,
      float customModelData,
      String leatherColor
  ) {
    this.material = material;
    this.amount = amount;
    this.displayName = displayName;
    this.lore = lore;
    this.enchantments = enchantments;
    this.unbreakable = unbreakable;

    this.potionColor = potionColor;
    this.potionEffects = potionEffects;

    this.customModelData = customModelData;
    this.leatherColor = leatherColor;
  }

  public String getMaterialName() {
    return material;
  }

  public int getAmount() {
    return amount;
  }

  public Color getPotionColor() {
    Color bukkitColor = null;

    if (potionColor != null) {
      ColorMap colorMap = ColorMap.getByColorName(potionColor);

      if (colorMap != null) {
        bukkitColor = colorMap.getColor();
      } else {
        try {
          bukkitColor = Color.fromRGB(Integer.parseInt(potionColor, 16));
        } catch (NumberFormatException exception) {
          LOGGER.error("Could not parse color " + potionColor + " for itemstack " + this,
              exception);
        }
      }
    }

    return bukkitColor;
  }

  public final Component getDisplayNameComponent() {
    return MiniMessage.miniMessage().deserialize(displayName);
  }

  public final List<Component> getLoreComponentList() {
    List<Component> loreList = new ArrayList<>();

    MiniMessage miniMessage = MiniMessage.miniMessage();
    for (String loreLine : lore) {
      loreList.add(miniMessage.deserialize(loreLine));
    }

    return loreList;
  }

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

    if (enchantments != null && !enchantments.isEmpty()) {
      for (SupplyTradeItemStackEnchantment enchantment : enchantments) {
        Enchantment bukkitEnchantment = enchantment.getEnchantment();

        if (bukkitEnchantment != null) {
          if (itemMeta instanceof EnchantmentStorageMeta) {
            ((EnchantmentStorageMeta) itemMeta).addStoredEnchant(bukkitEnchantment,
                enchantment.getLevel(), true);
          } else {
            itemMeta.addEnchant(bukkitEnchantment, enchantment.getLevel(), true);
          }
        }
      }
    }

    if (potionEffects != null && !potionEffects.isEmpty()
        && itemMeta instanceof PotionMeta potionMeta) {
      Color color = getPotionColor();
      if (color != null) {
        potionMeta.setColor(color);
      }

      for (SupplyTradeItemStackPotionEffect potionEffect : potionEffects) {
        potionMeta.addCustomEffect(potionEffect.getPotionEffect(), true);
      }
    }

    if (itemMeta != null && unbreakable) {
      itemMeta.setUnbreakable(true);
    }

    itemStack.setItemMeta(itemMeta);

    if (customModelData != -1f) {
      itemStack.setData(DataComponentTypes.CUSTOM_MODEL_DATA,
          CustomModelData.customModelData().addFloat(customModelData).build());
    }

    if (leatherColor != null) {
      Color colorHex = Color.fromRGB(Integer.parseInt(leatherColor, 16));
      itemStack.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor(colorHex, true));
    }

    return itemStack;
  }

  public String getDisplayName() {
    return displayName;
  }

  public List<String> getLore() {
    return lore;
  }

  public List<SupplyTradeItemStackEnchantment> getEnchantments() {
    return enchantments;
  }

  public boolean isUnbreakable() {
    return unbreakable;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (other == null || getClass() != other.getClass()) {
      return false;
    }

    SupplyTradeItemStack otherItemStack = (SupplyTradeItemStack) other;

    return amount == otherItemStack.amount && Objects.equals(material, otherItemStack.material) &&
        Objects.equals(displayName, otherItemStack.displayName) &&
        Objects.equals(lore, otherItemStack.lore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(material, amount, displayName, lore);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
