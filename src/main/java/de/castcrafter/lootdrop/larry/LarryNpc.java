package de.castcrafter.lootdrop.larry;

import lol.pyr.znpcsplus.api.NpcApi;
import lol.pyr.znpcsplus.api.NpcApiProvider;
import lol.pyr.znpcsplus.api.npc.NpcEntry;
import lol.pyr.znpcsplus.api.npc.NpcType;
import lol.pyr.znpcsplus.api.skin.SkinDescriptor;
import lol.pyr.znpcsplus.util.NpcLocation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;

public class LarryNpc {

  public static Location currentLocation = null;

  private static final int BOUNDING_BOX_RADIUS = 4;
  public static BoundingBox hideBoundingBox = null;

  private static final String NPC_ID = "larry";
  private static final Component NPC_NAME = Component.text("Larry",
      TextColor.fromHexString("#00FF40"));

  private static NpcEntry npc;

  public static void spawn(Location spawnLocation) {
    if (npc != null) {
      return;
    }

    NpcApi api = NpcApiProvider.get();
    NpcEntry byId = api.getNpcRegistry().getById(NPC_ID);

    if (byId != null) {
      npc = byId;
      return;
    }

    NpcType playerNpcType = api.getNpcTypeRegistry().getByName("player");
    npc = api.getNpcRegistry().create(
        NPC_ID,
        spawnLocation.getWorld(),
        playerNpcType,
        new NpcLocation(spawnLocation)
    );

    npc.enableEverything();

    SkinDescriptor skinDescriptor = api.getSkinDescriptorFactory().createUrlDescriptor(
        "https://www.minecraftskins.com/uploads/skins/2023/04/16/green-screen-man-21522853.png?v779",
        "slim"
    );

    npc.getNpc().setProperty(
        api.getPropertyRegistry().getByName("skin", SkinDescriptor.class),
        skinDescriptor
    );

    npc.getNpc().setProperty(
        api.getPropertyRegistry().getByName("name", Component.class),
        NPC_NAME
    );

    currentLocation = spawnLocation;
    updateBoundingBox();
  }

  public static void updateBoundingBox() {
    if (currentLocation == null) {
      return;
    }

    hideBoundingBox = new BoundingBox(
        currentLocation.getX() - BOUNDING_BOX_RADIUS,
        currentLocation.getY() - BOUNDING_BOX_RADIUS,
        currentLocation.getZ() - BOUNDING_BOX_RADIUS,
        currentLocation.getX() + BOUNDING_BOX_RADIUS,
        currentLocation.getY() + BOUNDING_BOX_RADIUS,
        currentLocation.getZ() + BOUNDING_BOX_RADIUS
    );
  }

  public static void updateLocation(Location location) {
    if (npc == null || npc.getNpc() == null) {
      spawn(location);
    }

    npc.getNpc().setLocation(new NpcLocation(location));

    currentLocation = location;
    updateBoundingBox();
  }

  public static void despawn() {
    NpcApiProvider.get().getNpcRegistry().delete(NPC_ID);
  }

  public static NpcEntry getNpc() {
    return npc;
  }

  public static Location locateNpc() {
    if (npc == null || npc.getNpc() == null) {
      return null;
    }

    return npc.getNpc().getLocation().toBukkitLocation(npc.getNpc().getWorld());
  }
}
