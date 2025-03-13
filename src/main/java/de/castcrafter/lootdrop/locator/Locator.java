package de.castcrafter.lootdrop.locator;

import it.unimi.dsi.fastutil.Pair;
import org.bukkit.Location;

public interface Locator<T> {

  /**
   * Locates the given object
   *
   * @param startingLocation The location to start the location from
   * @param toLocate         The object to locate
   * @return A pair of the result of the location where the first element is the direction and the
   * second element is the height difference
   */
  Pair<LocatorResult, LocatorResult> locate(Location startingLocation, T toLocate);

}
