package de.castcrafter.lootdrop.listener.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class JoinListener implements Listener {

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    if (event.getPlayer().hasPlayedBefore()) {
      return;
    }

    ItemStack item = new ItemStack(Material.PUMPKIN_PIE, 64);
    event.getPlayer().getInventory().addItem(item);
  }

}
