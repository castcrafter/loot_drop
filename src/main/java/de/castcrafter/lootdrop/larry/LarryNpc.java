package de.castcrafter.lootdrop.larry;

import lol.pyr.znpcsplus.api.NpcApi;
import lol.pyr.znpcsplus.api.NpcApiProvider;
import lol.pyr.znpcsplus.api.npc.NpcEntry;
import lol.pyr.znpcsplus.api.npc.NpcType;
import lol.pyr.znpcsplus.api.skin.SkinDescriptor;
import lol.pyr.znpcsplus.util.NpcLocation;
import org.bukkit.Location;

public class LarryNpc {

  private static final String NPC_ID = "larry";
  private static final String NPC_NAME = "Larry";

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

    npc.setProcessed(true);

    SkinDescriptor skinDescriptor = api.getSkinDescriptorFactory().createUrlDescriptor(
        "https://www.minecraftskins.com/uploads/skins/2025/03/08/green-23105935.png?v778",
        "slim"
    );

    npc.getNpc().setProperty(
        api.getPropertyRegistry().getByName("skin", SkinDescriptor.class),
        skinDescriptor
    );

    npc.getNpc().setProperty(
        api.getPropertyRegistry().getByName("name", String.class),
        NPC_NAME
    );
  }

  public static void updateLocation(Location location) {
    if (npc == null || npc.getNpc() == null) {
      return;
    }

    npc.getNpc().setLocation(new NpcLocation(location));
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
