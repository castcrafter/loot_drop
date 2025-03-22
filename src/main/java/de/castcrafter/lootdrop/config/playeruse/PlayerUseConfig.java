package de.castcrafter.lootdrop.config.playeruse;

import de.castcrafter.lootdrop.Main;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

@ConfigSerializable
public class PlayerUseConfig {

  private static final ComponentLogger LOGGER = ComponentLogger.logger(PlayerUseConfig.class);
  public static final PlayerUseConfig INSTANCE = new PlayerUseConfig();

  private transient YamlConfigurationLoader loader;
  private transient CommentedConfigurationNode node;

  private List<PlayerUses> uses = new ArrayList<>();

  public PlayerUseConfig() {
  }

  public void loadConfig() {
    loader = YamlConfigurationLoader.builder()
        .path(Main.getInstance().getDataFolder().toPath().resolve("uses.yml"))
        .build();
    try {
      node = loader.load();

      if (node == null) {
        LOGGER.error("Could not load configuration");

        throw new IllegalStateException("Could not load configuration");
      }

      PlayerUseConfig fetchedConfig = node.get(PlayerUseConfig.class);

      if (fetchedConfig == null) {
        LOGGER.error("Could not load configuration");

        throw new IllegalStateException("Could not load configuration");
      }

      uses = fetchedConfig.uses;
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

  public PlayerUses getUses(String recipeName) {
    return uses.stream().filter(playerUses -> playerUses.getRecipeName().equals(recipeName))
        .findFirst().orElseGet(() -> {
          PlayerUses playerUses = new PlayerUses(new ArrayList<>(), recipeName);
          uses.add(playerUses);
          return playerUses;
        });
  }

  public List<PlayerUses> getUses() {
    return uses;
  }
}
