package de.castcrafter.lootdrop.locator.locators;

import de.castcrafter.lootdrop.locator.Locator;
import de.castcrafter.lootdrop.locator.LocatorResult;
import de.castcrafter.lootdrop.locator.Locators;
import it.unimi.dsi.fastutil.Pair;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * A locator for entities
 */
public class EntityLocator implements Locator<Entity> {

  @Override
  public Pair<LocatorResult, LocatorResult> locate(Location startingLocation, Entity toLocate) {
    return Locators.getLocator(Location.class)
        .map(locator -> locator.locate(startingLocation, toLocate.getLocation()))
        .orElse(null);
  }
}
