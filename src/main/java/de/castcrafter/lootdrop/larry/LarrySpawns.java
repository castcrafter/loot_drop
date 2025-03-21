package de.castcrafter.lootdrop.larry;

import de.castcrafter.lootdrop.Main;
import java.security.SecureRandom;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public enum LarrySpawns {
  ONE(new Location(Bukkit.getWorlds().getFirst(), 37, 103, 197)),
  TWO(new Location(Bukkit.getWorlds().getFirst(), -113, 32, 310)),
  THREE(new Location(Bukkit.getWorlds().getFirst(), 185, 51, -70)),
  FOUR(new Location(Bukkit.getWorlds().getFirst(), 129, 152, 209)),
  FIVE(new Location(Bukkit.getWorlds().getFirst(), -136, 39, -2));

  private final Location location;

  LarrySpawns(Location location) {
    this.location = location;
  }

  private static boolean locationEquals(Location location1, Location location2) {
    if (location1 == null || location2 == null) {
      return false;
    }

    return location1.getBlockX() == location2.getBlockX()
        && location1.getBlockY() == location2.getBlockY()
        && location1.getBlockZ() == location2.getBlockZ();
  }

  public static Location getRandomLocation() {
    SecureRandom random = Main.getInstance().getRandom();

    Location oldLocation = LarryNpc.currentLocation;
    Location newLocation;

    do {
      newLocation = values()[random.nextInt(values().length)].getLocation();
    } while (locationEquals(oldLocation, newLocation));

    return newLocation;
  }

  public Location getLocation() {
    return location;
  }
}
