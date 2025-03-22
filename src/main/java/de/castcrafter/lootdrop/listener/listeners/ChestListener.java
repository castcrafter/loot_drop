package de.castcrafter.lootdrop.listener.listeners;

import java.util.Arrays;
import me.angeschossen.chestprotect.api.events.ProtectionPreCreationEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
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
  public void onChestLock(ProtectionPreCreationEvent event) {
    event.setCancelled(
        checkBlock(event.getLocation().getBlock(), event.getPlayer().getPlayer(), true));
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    event.setCancelled(checkBlock(event.getBlock(), event.getPlayer(), false));
  }

  @EventHandler
  public void onEntityExplode(EntityExplodeEvent event) {
    event.blockList().removeIf(block -> checkBlock(block, null, false));
  }

  @EventHandler
  public void onBlockExplode(BlockExplodeEvent event) {
    event.blockList().removeIf(block -> checkBlock(block, null, false));
  }

  private boolean checkBlock(Block block, Player player, boolean placed) {
    if (!Arrays.asList(CHEST_TYPES).contains(block.getType())) {
      return false;
    }

    if (!(block.getState() instanceof TileState tileState)) {
      return false;
    }

    PersistentDataContainer pdc = tileState.getPersistentDataContainer();
    if (pdc.has(LOOT_KEY, PersistentDataType.BOOLEAN) && player != null) {
      if (placed) {
        player.sendMessage(
            Component.text("Diese Kiste kann nicht protected werden. Sei kein Arsch!",
                NamedTextColor.RED)
        );

        return true;
      }

      player.sendMessage(Component.text("Diese Kiste kann nicht abgebaut werden. Sei kein Arsch!",
          NamedTextColor.RED));
    }

    return true;
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
