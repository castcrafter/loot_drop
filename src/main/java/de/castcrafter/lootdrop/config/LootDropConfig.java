package de.castcrafter.lootdrop.config;

import de.castcrafter.lootdrop.Main;
import de.castcrafter.lootdrop.config.drops.HourlyDrop;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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

	private long startedTimestampSeconds = ZonedDateTime.now().toEpochSecond();
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
			startedTimestampSeconds = fetchedConfig.startedTimestampSeconds;
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
	 * Gets drops.
	 *
	 * @return the drops
	 */
	public List<HourlyDrop> getDrops() {
		return drops;
	}

	/**
	 * Gets started timestamp.
	 *
	 * @return the started timestamp
	 */
	public ZonedDateTime getStartedTimestamp() {
		return ZonedDateTime.ofInstant(Instant.ofEpochSecond(startedTimestampSeconds), ZoneId.systemDefault());
	}

	/**
	 * Sets started timestamp.
	 *
	 * @param startedTimestamp the started timestamp
	 */
	public void setStartedTimestamp(ZonedDateTime startedTimestamp) {
		this.startedTimestampSeconds = startedTimestamp.toEpochSecond();
	}
}
