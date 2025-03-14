package de.castcrafter.lootdrop.config;

import de.castcrafter.lootdrop.Main;
import de.castcrafter.lootdrop.config.trades.SupplyTrade;
import de.castcrafter.lootdrop.timer.LootDropTimer;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

@ConfigSerializable
public class LootDropConfig {

  private static final ComponentLogger LOGGER = ComponentLogger.logger(LootDropConfig.class);
  public static final LootDropConfig INSTANCE = new LootDropConfig();

  private transient YamlConfigurationLoader loader;
  private transient CommentedConfigurationNode node;

  private long lastLarryRespawn = 0;
  private long respawnLarryEverySeconds = 10;

  private long startTimestampSeconds;
  private transient LootDropTimer timer;

  private List<SupplyTrade> trades = new ArrayList<>();

  public LootDropConfig() {
  }

  public void loadAndStartTimerIfExistsInConfig() {
    if (timer == null) {
      setTimer(new LootDropTimer());
      getTimer().start();
    }
  }

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

      trades = fetchedConfig.trades;
      startTimestampSeconds = fetchedConfig.startTimestampSeconds;
      respawnLarryEverySeconds = fetchedConfig.respawnLarryEverySeconds;
    } catch (Exception exception) {
      LOGGER.error("Failed to load configuration", exception);
    }
  }

  public void saveConfig() {
    try {
      node.set(this);
      loader.save(node);
    } catch (Exception exception) {
      LOGGER.error("Failed to save configuration", exception);
    }
  }

  public List<SupplyTrade> getTrades() {
    return trades;
  }

  public List<SupplyTrade> getTrades(long offset) {
    return trades.stream().filter(drop -> drop.getOffset() == offset).toList();
  }

  public void setStartTimestamp(ZonedDateTime startTimestamp) {
    this.startTimestampSeconds = startTimestamp.toEpochSecond();
  }

  public ZonedDateTime getStartTimestamp() {
    return ZonedDateTime.ofInstant(Instant.ofEpochSecond(startTimestampSeconds),
        ZoneId.systemDefault());
  }

  public long getRespawnLarryEverySeconds() {
    return respawnLarryEverySeconds;
  }

  public ZonedDateTime getLastLarryRespawn() {
    return ZonedDateTime.ofInstant(Instant.ofEpochSecond(lastLarryRespawn),
        ZoneId.systemDefault());
  }

  public void setLastLarryRespawn(ZonedDateTime lastLarryRespawn) {
    this.lastLarryRespawn = lastLarryRespawn.toEpochSecond();
  }

  public LootDropTimer getTimer() {
    return timer;
  }

  public void setTimer(LootDropTimer timer) {
    this.timer = timer;
  }
}
