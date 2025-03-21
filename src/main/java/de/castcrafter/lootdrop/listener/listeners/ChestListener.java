package de.castcrafter.lootdrop.listener.listeners;

import java.util.Arrays;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ChestListener implements Listener {

  private static final NamespacedKey LOOT_KEY = new NamespacedKey("lootdrop", "loot");

  private static final Material[] CHEST_TYPES = new Material[]{
      Material.CHEST,
      Material.BARREL
  };

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    Block block = event.getBlock();

    if (!Arrays.asList(CHEST_TYPES).contains(block.getType())) {
      return;
    }

    if (!(block.getState() instanceof TileState tileState)) {
      return;
    }

    PersistentDataContainer pdc = tileState.getPersistentDataContainer();
    if (pdc.has(LOOT_KEY, PersistentDataType.BOOLEAN)) {
      event.setCancelled(true);

      event.getPlayer()
          .sendMessage(Component.text("Diese Kiste kann nicht abgebaut werden. Sei kein Arsch!",
              NamedTextColor.RED));
    }
  }

  @EventHandler
  public void onLootGenerate(LootGenerateEvent event) {
    Block block = event.getLootContext().getLocation().getBlock();

    if (!Arrays.asList(CHEST_TYPES).contains(block.getType())) {
      return;
    }

    if (!(block.getState() instanceof TileState tileState)) {
      return;
    }

    PersistentDataContainer pdc = tileState.getPersistentDataContainer();
    pdc.set(LOOT_KEY, PersistentDataType.BOOLEAN, true);
  }

}
