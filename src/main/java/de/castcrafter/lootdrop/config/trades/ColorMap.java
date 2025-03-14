package de.castcrafter.lootdrop.config.trades;

import java.util.Arrays;
import org.bukkit.Color;

/**
 * The enum Color map.
 */
public enum ColorMap {

  WHITE(Color.WHITE, "white"),
  SILVER(Color.SILVER, "silver"),
  GRAY(Color.GRAY, "gray"),
  BLACK(Color.BLACK, "black"),
  RED(Color.RED, "red"),
  MAROON(Color.MAROON, "maroon"),
  YELLOW(Color.YELLOW, "yellow"),
  OLIVE(Color.OLIVE, "olive"),
  LIME(Color.LIME, "lime"),
  GREEN(Color.GREEN, "green"),
  AQUA(Color.AQUA, "aqua"),
  TEAL(Color.TEAL, "teal"),
  BLUE(Color.BLUE, "blue"),
  NAVY(Color.NAVY, "navy"),
  FUCHSIA(Color.FUCHSIA, "fuchsia"),
  PURPLE(Color.PURPLE, "purple"),
  ORANGE(Color.ORANGE, "orange");

  private final Color color;
  private final String colorName;

  ColorMap(Color color, String colorName) {
    this.color = color;
    this.colorName = colorName;
  }

  public static ColorMap getByBukkitColor(Color color) {
    return Arrays.stream(values())
        .filter(value -> value.getColor().equals(color))
        .findFirst()
        .orElse(null);
  }

  public static ColorMap getByColorName(String colorName) {
    return Arrays.stream(values())
        .filter(value -> value.getColorName().equalsIgnoreCase(colorName))
        .findFirst()
        .orElse(null);
  }

  public Color getColor() {
    return color;
  }

  public String getColorName() {
    return colorName;
  }
}
