package de.castcrafter.lootdrop.larry;

import de.castcrafter.lootdrop.Main;
import java.security.SecureRandom;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public enum LarrySpawns {
  ONE(new Location(Bukkit.getWorlds().getFirst(), 37, 53, 1403)),
  TWO(new Location(Bukkit.getWorlds().getFirst(), -936, 58, 597)),
  THREE(new Location(Bukkit.getWorlds().getFirst(), 75, 63, -1666)),
  FOUR(new Location(Bukkit.getWorlds().getFirst(), 947, 136, -606)),
  FIVE(new Location(Bukkit.getWorlds().getFirst(), 1294, 61, 942)),
  SIX(new Location(Bukkit.getWorlds().getFirst(), -1784, 123, -1630)),
  SEVEN(new Location(Bukkit.getWorlds().getFirst(), -314, 54, -218)),
  EIGHT(new Location(Bukkit.getWorlds().getFirst(), -202, 94, 264)),
  NINE(new Location(Bukkit.getWorlds().getFirst(), 373, 36, -388));

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

    return newLocation.clone().add(0.5, 0, 0.5);
  }

  public Location getLocation() {
    return location;
  }
}
