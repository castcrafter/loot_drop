package de.castcrafter.lootdrop.larry;

import de.castcrafter.lootdrop.gui.drops.DropsGui;
import lol.pyr.znpcsplus.api.event.NpcInteractEvent;
import lol.pyr.znpcsplus.api.npc.Npc;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LarryListener implements Listener {

  @EventHandler
  public void onLarryInteract(NpcInteractEvent event) {
    Npc npc = event.getNpc();

    if (npc != LarryNpc.getNpc().getNpc()) {
      return;
    }

    Player player = event.getPlayer();

    new DropsGui(player).show(player);
  }

}
