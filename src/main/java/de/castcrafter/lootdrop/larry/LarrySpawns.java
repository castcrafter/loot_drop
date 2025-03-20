package de.castcrafter.lootdrop.larry;

import de.castcrafter.lootdrop.Main;
import java.security.SecureRandom;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public enum LarrySpawns {
  SPAWN(new Location(Bukkit.getWorlds().getFirst(), -29, 73, -118)),
  X_PLUS_TEN(new Location(Bukkit.getWorlds().getFirst(), -19, 73, -118)),
  X_MINUS_TEN(new Location(Bukkit.getWorlds().getFirst(), -39, 73, -118)),
  Z_PLUS_TEN(new Location(Bukkit.getWorlds().getFirst(), -29, 73, -108)),
  Z_MINUS_TEN(new Location(Bukkit.getWorlds().getFirst(), -29, 73, -128));

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
