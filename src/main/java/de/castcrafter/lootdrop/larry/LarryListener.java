package de.castcrafter.lootdrop.larry;

import de.castcrafter.lootdrop.Main;
import de.castcrafter.lootdrop.gui.drops.DropsGui;
import lol.pyr.znpcsplus.api.event.NpcInteractEvent;
import lol.pyr.znpcsplus.api.npc.Npc;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class LarryListener implements Listener {

  @EventHandler
  public void onMove(PlayerMoveEvent event) {
    if (!event.hasChangedBlock()) {
      return;
    }

    if (LarryNpc.hideBoundingBox.contains(event.getTo().toVector())) {
      hideShowAll(event.getPlayer(), true);
      return;
    }

    hideShowAll(event.getPlayer(), false);
  }

  private void hideShowAll(Player player, boolean show) {
    if (show) {
      Bukkit.getOnlinePlayers().forEach(it -> it.showPlayer(Main.getInstance(), player));
      return;
    }

    Bukkit.getOnlinePlayers().forEach(it -> it.hidePlayer(Main.getInstance(), player));
  }

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
