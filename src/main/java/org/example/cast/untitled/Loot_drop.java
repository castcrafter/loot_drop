package org.example.cast.untitled;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.inventory.EquipmentSlot.*;

public final class Loot_drop extends JavaPlugin implements TabExecutor, Listener {

    private List<ItemStack> lootContents = new ArrayList<>();
    private ArmorStand lootArmorStand;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("copyloot").setExecutor(this);
        this.getCommand("summonloot").setExecutor(this);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        if (label.equalsIgnoreCase("copyloot")) {
            return copyLoot(player);
        } else if (label.equalsIgnoreCase("summonloot")) {
            if (args.length < 1) {
                player.sendMessage("Please specify the y offset.");
                return true;
            }

            int yOffset;
            try {
                yOffset = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                player.sendMessage("Invalid y offset. Please enter a number.");
                return true;
            }

            return summonLoot(player, yOffset);
        }
        return false;
    }

    private boolean copyLoot(Player player) {
        Block targetBlock = player.getTargetBlockExact(5);  // Looking for block within 5 blocks range
        if (targetBlock == null || targetBlock.getType() != Material.CHEST) {
            player.sendMessage("You must be looking at a chest.");
            return true;
        }

        Chest chest = (Chest) targetBlock.getState();
        Inventory chestInventory = chest.getInventory();
        lootContents.clear();
        for (ItemStack item : chestInventory.getContents()) {
            if (item != null) {
                lootContents.add(item.clone());
            }
        }

        player.sendMessage("Loot copied from the chest.");
        return true;
    }

    private boolean summonLoot(Player player, int yOffset) {
        if (lootContents.isEmpty()) {
            player.sendMessage("No loot to drop. Copy loot from a chest first.");
            return true;
        }

        Location playerLocation = player.getLocation();
        Location lootLocation = playerLocation.clone().add(0, yOffset, 0);

        ItemStack heartOfTheSea = new ItemStack(Material.HEART_OF_THE_SEA);
        ItemMeta meta = heartOfTheSea.getItemMeta();
        if (meta != null) {
            meta.setCustomModelData(1);
            heartOfTheSea.setItemMeta(meta);
        }

        lootArmorStand = lootLocation.getWorld().spawn(lootLocation, ArmorStand.class);
        lootArmorStand.setInvisible(true);
        lootArmorStand.setDisabledSlots(HEAD,CHEST,FEET,HAND,OFF_HAND,LEGS);
        lootArmorStand.setCustomName("LootDrop");
        lootArmorStand.setCustomNameVisible(false);
        lootArmorStand.getEquipment().setHelmet(heartOfTheSea);

        // Schedule a task to update the velocity and spawn particles every tick
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            lootArmorStand.setVelocity(new Vector(0, -0.03, 0));
            lootArmorStand.getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, lootArmorStand.getLocation().add(0, 6, 0), 1, 0.3, 0, 0.3, 0);
        }, 0L, 1L);

        // Send title and subtitle to all online players
        String title = "Loot Drop";
        String subtitle = "Location: " + lootLocation.getBlockX() + ", " + lootLocation.getBlockY() + ", " + lootLocation.getBlockZ();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendTitle(title, subtitle, 10, 200, 20);
            onlinePlayer.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1f, 1f);
        }
        return true;
    }



    @EventHandler
    public void onArmorStandDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof ArmorStand && event.getEntity().getCustomName() != null && event.getEntity().getCustomName().equals("LootDrop")) {
            if (event.getDamager() instanceof Player) {
                Player player = (Player) event.getDamager();
                ArmorStand armorStand = (ArmorStand) event.getEntity();
                Location location = armorStand.getLocation();
                for (ItemStack item : lootContents) {
                    if (item != null) {
                        location.getWorld().dropItem(location, item);
                    }
                }
                armorStand.remove();

                player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 0.5f, 0.1f);
            }
        }
    }
}
