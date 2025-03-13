package de.castcrafter.lootdrop.locator.locators;

import de.castcrafter.lootdrop.locator.Locator;
import de.castcrafter.lootdrop.locator.LocatorResult;
import it.unimi.dsi.fastutil.Pair;
import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * A locator for locations
 */
public class LocationLocator implements Locator<Location> {

  @Override
  public Pair<LocatorResult, LocatorResult> locate(Location startingLocation, Location toLocate) {
    if (startingLocation.getWorld() != toLocate.getWorld()) {
      return Pair.of(LocatorResult.WORLD, LocatorResult.WORLD);
    }

    int yDiff = startingLocation.getBlockY() - toLocate.getBlockY();
    LocatorResult yResult = switch (Integer.compare(yDiff, 0)) {
      case -1 -> LocatorResult.UP;
      case 1 -> LocatorResult.DOWN;
      default -> LocatorResult.NONE;
    };

    LocatorResult directionResult = determineDirection(startingLocation, toLocate);

    return Pair.of(directionResult, yResult);
  }

  private LocatorResult determineDirection(Location playerLocation, Location targetLocation) {
    if (playerLocation.getWorld() != targetLocation.getWorld()) {
      return LocatorResult.WORLD;
    }

    // Falls sich das Ziel genau an der gleichen X/Z-Position befindet
    if (playerLocation.getBlockX() == targetLocation.getBlockX()
        && playerLocation.getBlockZ() == targetLocation.getBlockZ()) {
      return LocatorResult.NONE;
    }

    // Positionsdifferenz berechnen
    double diffX = targetLocation.getX() - playerLocation.getX();
    double diffZ = targetLocation.getZ() - playerLocation.getZ();

    // Spielerblickrichtung holen
    Vector direction = playerLocation.getDirection();
    double forwardX = direction.getX();
    double forwardZ = direction.getZ();

    // Skalarprodukte berechnen
    double forwardDot = (diffX * forwardX) + (diffZ * forwardZ);
    double rightDot = (diffX * -forwardZ) + (diffZ * forwardX); // Rechts-Vektor (-Z, X)

    // Richtung bestimmen
    boolean isForward = forwardDot > 0;
    boolean isRight = rightDot > 0;

    if (Math.abs(forwardDot) > Math.abs(rightDot)) {
      // Ziel liegt stärker in Blickrichtung als seitlich
      if (Math.abs(rightDot) > 0.5 * Math.abs(forwardDot)) {
        return isForward ? (isRight ? LocatorResult.FRONT_RIGHT : LocatorResult.FRONT_LEFT)
            : (isRight ? LocatorResult.BACK_RIGHT : LocatorResult.BACK_LEFT);
      } else {
        return isForward ? LocatorResult.FRONT : LocatorResult.BACK;
      }
    } else {
      // Ziel liegt stärker seitlich als in Blickrichtung
      if (Math.abs(forwardDot) > 0.5 * Math.abs(rightDot)) {
        return isRight ? (isForward ? LocatorResult.FRONT_RIGHT : LocatorResult.BACK_RIGHT)
            : (isForward ? LocatorResult.FRONT_LEFT : LocatorResult.BACK_LEFT);
      } else {
        return isRight ? LocatorResult.RIGHT : LocatorResult.LEFT;
      }
    }
  }
}
