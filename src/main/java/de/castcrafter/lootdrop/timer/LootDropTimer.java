package de.castcrafter.lootdrop.timer;

import de.castcrafter.lootdrop.Main;
import de.castcrafter.lootdrop.config.LootDropConfig;
import de.castcrafter.lootdrop.config.trades.SupplyTrade;
import de.castcrafter.lootdrop.larry.LarryNpc;
import de.castcrafter.lootdrop.larry.LarrySpawns;
import de.castcrafter.lootdrop.utils.Chat;
import de.castcrafter.lootdrop.utils.SoundUtils;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LootDropTimer extends BukkitRunnable {

  private static final ComponentLogger LOGGER = ComponentLogger.logger();

  @Override
  public void run() {
    ZonedDateTime currentTime = ZonedDateTime.now();
    ZonedDateTime startTime = LootDropConfig.INSTANCE.getStartTimestamp();

    if (currentTime.isBefore(startTime)) {
      return;
    }

    ZonedDateTime lastLarryRespawn = LootDropConfig.INSTANCE.getLastLarryRespawn();
    long respawnLarryEverySeconds = LootDropConfig.INSTANCE.getRespawnLarryEverySeconds();

    if (Duration.between(lastLarryRespawn, currentTime).toSeconds() >= respawnLarryEverySeconds) {
      LootDropConfig.INSTANCE.setLastLarryRespawn(currentTime);
      LarryNpc.updateLocation(LarrySpawns.getRandomLocation());

      for (Player player : Bukkit.getOnlinePlayers()) {
        Chat.sendMessage(player,
            Component.text("Larry wurde an einem neuen Ort gesichtet!", NamedTextColor.GOLD));
        SoundUtils.playSound(player, Sound.ENTITY_CHICKEN_EGG, 1, 1);
      }
    }

    long currentOffset = Duration.between(startTime, currentTime).toSeconds();
    List<SupplyTrade> trades = LootDropConfig.INSTANCE.getTrades(currentOffset);

    if (!trades.isEmpty()) {
      int count = trades.size();

      for (Player player : Bukkit.getOnlinePlayers()) {
        Chat.sendMessage(
            player,
            Component.text("Es wurden ", NamedTextColor.GOLD)
                .append(Component.text(count, NamedTextColor.GREEN))
                .append(Component.text(" neue Trades geöffnet!", NamedTextColor.GOLD))
        );

        SoundUtils.playSound(player, Sound.ENTITY_CHICKEN_EGG, 1, 1);
      }
    }
  }

  public void stop() {
    try {
      if (!this.isCancelled()) {
        this.cancel();
      } else {
        LOGGER.error("Could not stop timer, already cancelled");
      }
    } catch (Exception ignored) {
      LOGGER.error("Could not stop timer, already cancelled");
    }
  }

  public void start() {
    if (this.isRunning()) {
      LOGGER.error("Could not start timer, already running");
      return;
    }

    this.runTaskTimerAsynchronously(Main.getInstance(), 0, 20L);
  }

  public boolean isRunning() {
    try {
      return !this.isCancelled();
    } catch (Exception ignored) {
      return false;
    }
  }
}
