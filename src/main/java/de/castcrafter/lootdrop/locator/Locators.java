package de.castcrafter.lootdrop.locator;

import de.castcrafter.lootdrop.locator.locators.LocationLocator;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.Optional;
import org.bukkit.Location;

public class Locators {

  public static final Map<LocatorResult, String> LOCATOR_RESULT_DISPLAY;
  private static final Object2ObjectMap<Class<?>, Locator<?>> locators;

  static {
    LOCATOR_RESULT_DISPLAY = new Object2ObjectOpenHashMap<>();
    locators = new Object2ObjectOpenHashMap<>();

    // Result Displays
    LOCATOR_RESULT_DISPLAY.put(LocatorResult.NONE, "☑");
    LOCATOR_RESULT_DISPLAY.put(LocatorResult.WORLD, "☢");

    LOCATOR_RESULT_DISPLAY.put(LocatorResult.UP, "↑");
    LOCATOR_RESULT_DISPLAY.put(LocatorResult.DOWN, "↓");

    LOCATOR_RESULT_DISPLAY.put(LocatorResult.RIGHT, "→");
    LOCATOR_RESULT_DISPLAY.put(LocatorResult.FRONT_RIGHT, "↗");
    LOCATOR_RESULT_DISPLAY.put(LocatorResult.FRONT, "↑");
    LOCATOR_RESULT_DISPLAY.put(LocatorResult.FRONT_LEFT, "↖");
    LOCATOR_RESULT_DISPLAY.put(LocatorResult.LEFT, "←");
    LOCATOR_RESULT_DISPLAY.put(LocatorResult.BACK_LEFT, "↙");
    LOCATOR_RESULT_DISPLAY.put(LocatorResult.BACK, "↓");
    LOCATOR_RESULT_DISPLAY.put(LocatorResult.BACK_RIGHT, "↘");

    // Locators
    locators.put(Location.class, new LocationLocator());
  }

  /**
   * Gets the locator for the given class
   *
   * @param clazz The class to get the locator for
   * @param <T>   The type of the locator
   * @return The locator
   */
  @SuppressWarnings("unchecked")
  public static <T> Optional<Locator<T>> getLocator(Class<T> clazz) {
    return Optional.ofNullable((Locator<T>) locators.get(clazz));
  }

}
