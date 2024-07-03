package de.castcrafter.lootdrop.config;

import de.castcrafter.lootdrop.Main;
import de.castcrafter.lootdrop.config.drops.HourlyDrop;
import de.castcrafter.lootdrop.config.drops.HourlyDropItemStack;
import de.castcrafter.lootdrop.config.drops.HourlyDropRecipe;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The type Loot drop config.
 */
@ConfigSerializable
public class LootDropConfig {

	private static final ComponentLogger LOGGER = ComponentLogger.logger(LootDropConfig.class);
	public static final LootDropConfig INSTANCE = new LootDropConfig();

	private transient YamlConfigurationLoader loader;
	private transient CommentedConfigurationNode node;

	private List<HourlyDrop> drops = new ArrayList<>();

	/**
	 * Instantiates a new Loot drop config.
	 */
	public LootDropConfig() {
	}

	/**
	 * Load config loot drop config.
	 */
	public void loadConfig() {
		loader = YamlConfigurationLoader.builder()
										.path(Main.getInstance().getDataFolder().toPath().resolve("config.yml"))
										.build();
		try {
			node = loader.load();

			if (node == null) {
				LOGGER.error("Could not load configuration");

				throw new IllegalStateException("Could not load configuration");
			}

			LootDropConfig fetchedConfig = node.get(LootDropConfig.class);

			if (fetchedConfig == null) {
				LOGGER.error("Could not load configuration");

				throw new IllegalStateException("Could not load configuration");
			}

			drops = fetchedConfig.drops;
		} catch (Exception exception) {
			LOGGER.error("Failed to load configuration", exception);
		}
	}

	/**
	 * Save config.
	 */
	public void saveConfig() {
		try {
			node.set(this);
			loader.save(node);
		} catch (Exception exception) {
			LOGGER.error("Failed to save configuration", exception);
		}
	}

	/**
	 * Sets default config.
	 */
	public void setDefaultConfig() {
		HourlyDropItemStack itemStack = new HourlyDropItemStack("stone", 1, "Stone", List.of());

		for (int i = 0; i < 2; i++) {
			List<HourlyDropRecipe> recipes = new ArrayList<>();
			recipes.add(new HourlyDropRecipe(itemStack, itemStack, itemStack, 1, new HashMap<>()));

			HourlyDrop hourlyDrop = new HourlyDrop(i, recipes);
			drops.add(hourlyDrop);
		}

		LOGGER.info("Default configuration set");
	}

	/**
	 * Gets drops.
	 *
	 * @return the drops
	 */
	public List<HourlyDrop> getDrops() {
		return drops;
	}
}
