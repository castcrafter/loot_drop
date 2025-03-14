package de.castcrafter.lootdrop.larry;

import de.castcrafter.lootdrop.Main;
import java.security.SecureRandom;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public enum LarrySpawns {
  SPAWN(new Location(Bukkit.getWorlds().getFirst(), 78, 71, -63)),
  X_PLUS_TEN(new Location(Bukkit.getWorlds().getFirst(), 88, 71, -63)),
  X_MINUS_TEN(new Location(Bukkit.getWorlds().getFirst(), 68, 71, -63)),
  Z_PLUS_TEN(new Location(Bukkit.getWorlds().getFirst(), 78, 71, -53)),
  Z_MINUS_TEN(new Location(Bukkit.getWorlds().getFirst(), 78, 71, -73));

  private final Location location;

  LarrySpawns(Location location) {
    this.location = location;
  }

  public static Location getRandomLocation() {
    SecureRandom random = Main.getInstance().getRandom();

    return values()[random.nextInt(values().length)].getLocation();
  }

  public Location getLocation() {
    return location;
  }
}
