package de.castcrafter.lootdrop;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.bukkit.inventory.EquipmentSlot.CHEST;
import static org.bukkit.inventory.EquipmentSlot.FEET;
import static org.bukkit.inventory.EquipmentSlot.HAND;
import static org.bukkit.inventory.EquipmentSlot.HEAD;
import static org.bukkit.inventory.EquipmentSlot.LEGS;
import static org.bukkit.inventory.EquipmentSlot.OFF_HAND;

public class Loot_drop implements Listener, CommandExecutor {

	private Main plugin;

	public Loot_drop(Main plugin) {
		this.plugin = plugin;
	}

	private List<ItemStack> goodLootContents = new ArrayList<>();
	private List<ItemStack> badLootContents = new ArrayList<>();
	private ArmorStand lootArmorStand;
	private int particleTaskId;
	private double goodProbability;
	private double badProbability;
	private UUID lootArmorStandUUID;

	public void onEnable() {
		Objects.requireNonNull(plugin.getCommand("copyloot")).setExecutor(this);
		Objects.requireNonNull(plugin.getCommand("summonloot")).setExecutor(this);
		Objects.requireNonNull(plugin.getCommand("seamine")).setExecutor(this);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public void onDisable() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!( sender instanceof Player )) {
			sender.sendMessage("This command can only be used by players.");
			return true;
		}

		Player player = (Player) sender;
		if (label.equalsIgnoreCase("copyloot")) {
			if (args.length < 1) {
				player.sendMessage("Please specify the loot type (good or bad).");
				return true;
			}

			String lootType = args[ 0 ].toLowerCase();
			if (!lootType.equals("good") && !lootType.equals("bad")) {
				player.sendMessage("Invalid loot type. Please enter 'good' or 'bad'.");
				return true;
			}

			return copyLoot(player, lootType);
		} else if (label.equalsIgnoreCase("summonloot")) {
			if (args.length < 3) {
				player.sendMessage("Please specify the y offset and the probabilities.");
				return true;
			}

			int yOffset;
			try {
				yOffset = Integer.parseInt(args[ 0 ]);
			} catch (NumberFormatException e) {
				player.sendMessage("Invalid y offset. Please enter a number.");
				return true;
			}

			double goodProbability, badProbability;
			try {
				goodProbability = Double.parseDouble(args[ 1 ]);
				badProbability = Double.parseDouble(args[ 2 ]);
			} catch (NumberFormatException e) {
				player.sendMessage("Invalid probabilities. Please enter numbers.");
				return true;
			}

			return summonLoot(player, yOffset, goodProbability, badProbability);
		} else if (label.equalsIgnoreCase("seamine")) {
			return summonMine((Location) player.getLocation());
		}
//        else if (label.equalsIgnoreCase("sealoot")) {
//            if (!(sender instanceof Player)) {
//                sender.sendMessage("This command can only be used by players.");
//                return true;
//            }
//
//            int numMines = 0;
//            if (args.length > 0) {
//                try {
//                    numMines = Integer.parseInt(args[0]);
//                } catch (NumberFormatException e) {
//                    player.sendMessage(ChatColor.RED + "Invalid number of mines. Please enter a number.");
//                    return true;
//                }
//            }
//
//            return placeSeaLoot(player, numMines);
//        }
		return false;
	}

	//    private boolean placeSeaLoot(Player player, int numMines) {
//        Location playerLocation = player.getLocation();
//        World world = playerLocation.getWorld();
//
//        // Find the top of the water
//        int y = world.getHighestBlockYAt(playerLocation);
//        while (world.getBlockAt(playerLocation.getBlockX(), y, playerLocation.getBlockZ()).getType() != Material.WATER) {
//            y++;
//        }
//        Location pasteLocation = new Location(world, playerLocation.getX(), y + 1, playerLocation.getBlockZ());
//
//        File schematic = new File(Objects.requireNonNull(getServer().getPluginManager().getPlugin("WorldEdit")).getDataFolder() + File.separator + "schematics", "raft.schem");
//        if (!schematic.exists()) {
//            player.sendMessage(ChatColor.RED + "Schematic not found.");
//            return true;
//        }
//
//        try {
//            WorldEdit worldEdit = WorldEdit.getInstance();
//            ClipboardFormat format = ClipboardFormats.findByFile(schematic);
//            assert format != null;
//            try (ClipboardReader reader = format.getReader(Files.newInputStream(schematic.toPath()))) {
//                Clipboard clipboard = reader.read();
//
//                try (EditSession editSession = worldEdit.getEditSessionFactory().getEditSession(new BukkitWorld(player.getWorld()), -1)) {
//                    Operation operation = new ClipboardHolder(clipboard)
//                            .createPaste(editSession)
//                            .to(BlockVector3.at(pasteLocation.getX(), pasteLocation.getY(), pasteLocation.getZ()))
//                            .ignoreAirBlocks(false)
//                            .build();
//                    Operations.complete(operation);
//                } catch (WorldEditException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//
//            player.sendMessage(ChatColor.GREEN + "Schematic placed successfully.");
//        } catch (IOException e) {
//            player.sendMessage(ChatColor.RED + "An error occurred while placing the schematic.");
//            e.printStackTrace();
//        }
//        for (int i = 0; i < numMines; i++) {
//            // Generate a random location within a 30 block radius
//            double dx = Math.random() * 60 - 20;
//            double dz = Math.random() * 60 - 20;
//            Location mineLocation = player.getLocation().add(dx, 0, dz);
//
//            // Find the top of the water at the mine location
//            while (mineLocation.getWorld().getBlockAt(mineLocation.getBlockX(), y, mineLocation.getBlockZ()).getType() != Material.WATER) {
//                y++;
//            }
//            double dy = Math.random() * 10;
//            dy = Math.min(dy, y - 2);
//            mineLocation.setY(y - dy);
//
//            summonMine(mineLocation);
//        }
//
//        return true;
//    }
	private boolean copyLoot(Player player, String lootType) {
		Block targetBlock = player.getTargetBlockExact(5);
		if (targetBlock == null || targetBlock.getType() != Material.CHEST) {
			player.sendMessage("You must be looking at a chest.");
			return true;
		}

		Chest chest = (Chest) targetBlock.getState();
		Inventory chestInventory = chest.getInventory();
		List<ItemStack> lootContents = lootType.equals("good") ? goodLootContents : badLootContents;
		lootContents.clear();
		for (ItemStack item : chestInventory.getContents()) {
			if (item != null) {
				lootContents.add(item.clone());
			}
		}

		player.sendMessage("Loot copied from the chest to the " + lootType + " loot table.");
		return true;
	}


	private boolean summonLoot(Player player, int yOffset, double goodProbability, double badProbability) {
		this.goodProbability = goodProbability;
		this.badProbability = badProbability;
		if (goodProbability + badProbability != 1.0) {
			player.sendMessage("Invalid probabilities. They should add up to 1.0.");
			return true;
		}

		List<ItemStack> lootContents;
		double random = Math.random();
		if (random < goodProbability) {
			lootContents = goodLootContents;
		} else {
			lootContents = badLootContents;
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
		lootArmorStandUUID = lootArmorStand.getUniqueId();
		lootArmorStand.setInvisible(true);
		lootArmorStand.setDisabledSlots(HEAD, CHEST, FEET, HAND, OFF_HAND, LEGS);
		lootArmorStand.getEquipment().setHelmet(heartOfTheSea);

		particleTaskId = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
			lootArmorStand.setVelocity(new Vector(0, -0.03, 0));
			lootArmorStand.getWorld()
						  .spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, lootArmorStand.getLocation().add(0, 6, 0), 1,
										 0.3, 0, 0.3, 0
						  );
		}, 0L, 1L).getTaskId();

		String title = "Loot Drop";
		String subtitle = "Location: " + lootLocation.getBlockX() + ", " + lootLocation.getBlockY() + ", " +
						  lootLocation.getBlockZ();
		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			onlinePlayer.sendTitle(title, subtitle, 10, 200, 20);
			onlinePlayer.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1f, 1f);
		}
		return true;
	}

	@EventHandler
	public void onArmorStandDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity().getUniqueId().equals(lootArmorStandUUID)) {
			if (event.getDamager() instanceof Player) {
				Player player = (Player) event.getDamager();
				ArmorStand armorStand = (ArmorStand) event.getEntity();
				Location location = armorStand.getLocation();
				List<ItemStack> lootContents;
				double random = Math.random();
				if (random < this.goodProbability) {
					lootContents = goodLootContents;
				} else {
					lootContents = badLootContents;
				}
				for (ItemStack item : lootContents) {
					if (item != null) {
						if (item.getType() == Material.TNT) {
							for (int i = 0; i < item.getAmount(); i++) {
								location.getWorld().spawn(location, TNTPrimed.class).setFuseTicks(1);
							}
						} else if (item.getType().name().endsWith("_SPAWN_EGG")) {
							EntityType entityType = EntityType.valueOf(item.getType().name().replace("_SPAWN_EGG", ""));
							for (int i = 0; i < item.getAmount(); i++) {
								location.getWorld().spawnEntity(location, entityType);
							}
						} else if (item.getType() == Material.BARRIER) {
							for (int x = -2; x <= 2; x++) {
								for (int y = -5; y <= 5; y++) {
									for (int z = -2; z <= 2; z++) {
										location.clone().add(x, y, z).getBlock().setType(Material.AIR);
									}
								}
							}
						} else {
							location.getWorld().dropItemNaturally(location, item);
						}
					}
				}
				Bukkit.getScheduler().cancelTask(particleTaskId);
				armorStand.remove();

				player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 0.5f, 0.1f);
			}
		}
	}

	private boolean summonMine(Location location) {
		// Round the coordinates to the nearest block
		System.out.println("Summoning mine at: " + location);
		double x = Math.floor(location.getX()) + 0.5;
		double y = Math.floor(location.getY());
		double z = Math.floor(location.getZ()) + 0.5;
		Location roundedLocation = new Location(location.getWorld(), x, y, z);

		ItemStack heartOfTheSea = new ItemStack(Material.HEART_OF_THE_SEA);
		ItemMeta meta = heartOfTheSea.getItemMeta();
		if (meta != null) {
			meta.setCustomModelData(2);
			heartOfTheSea.setItemMeta(meta);
		}

		ArmorStand armorStand = roundedLocation.getWorld().spawn(roundedLocation, ArmorStand.class);
		armorStand.setInvisible(true);
		armorStand.setGravity(false);
		armorStand.addScoreboardTag("seamine");
		armorStand.setDisabledSlots(HEAD, CHEST, FEET, HAND, OFF_HAND, LEGS);
		armorStand.getEquipment().setHelmet(heartOfTheSea);

		// Create chains from the armor stand down to the first solid block
		Location chainLocation = armorStand.getLocation().clone();
		chainLocation.setX(Math.floor(chainLocation.getX()));
		chainLocation.setZ(Math.floor(chainLocation.getZ()));
		while (!chainLocation.getBlock().getType().isSolid()) {
			chainLocation.getBlock().setType(Material.CHAIN);
			chainLocation.subtract(0, 1, 0);
		}

		return true;
	}

	@EventHandler
	public void onPlayerApproachMine(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (player.getGameMode() == GameMode.SURVIVAL) {
			double range = 5.0; // Set the range
			for (Entity entity : player.getNearbyEntities(range, range, range)) {
				if (entity instanceof ArmorStand) {
					ArmorStand armorStand = (ArmorStand) entity;
					if (armorStand.getScoreboardTags().contains("seamine")) {
						for (int i = 0; i < 10; i++) {
							armorStand.getWorld().spawn(armorStand.getLocation(), TNTPrimed.class).setFuseTicks(2);
						}
						armorStand.remove();
						break;
					}
				}
			}
		}
	}

}