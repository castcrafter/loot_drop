package de.castcrafter.lootdrop.duel;

import de.castcrafter.lootdrop.Main;
import de.castcrafter.lootdrop.utils.SoundUtils;
import java.util.List;
import java.util.Objects;
import kr.toxicity.hud.api.BetterHud;
import kr.toxicity.hud.api.hud.Hud;
import kr.toxicity.hud.api.player.HudPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * The type Duel vote timer.
 */
public class DuelVoteTimer extends BukkitRunnable implements Listener {

  private final int[] plingSounds = new int[]{
      120, 90, 60, 30, 10, 5, 4, 3, 2, 1
  };

  private final Duel duel;
  private final int maxSeconds;
  private int currentSeconds;

  /**
   * Instantiates a new Duel vote timer.
   *
   * @param duel       the duel
   * @param maxSeconds the max seconds
   */
  public DuelVoteTimer(Duel duel, int maxSeconds) {
    this.duel = duel;

    this.currentSeconds = maxSeconds + 1;
    this.maxSeconds = maxSeconds;

    Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
  }

  /**
   * Is running boolean.
   *
   * @return the boolean
   */
  public boolean isRunning() {
    return this.currentSeconds > 0;
  }

  @Override
  public void run() {
    currentSeconds--;

    plingSound();

    if (currentSeconds <= 0) {
      duel.endVote();
      stop();
    }
  }

  /**
   * Pling sound.
   */
  private void plingSound() {
    for (int plingSound : plingSounds) {
      if (currentSeconds == plingSound) {
        Bukkit.getOnlinePlayers().forEach(player -> {
          SoundUtils.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, .5f, 1f);
        });
      }
    }
  }

  /**
   * Start.
   */
  public void start() {
    this.runTaskTimer(Main.getInstance(), 0, 20L);

    Hud hud = getHud();
    List<HudPlayer> hudPlayers = Bukkit.getOnlinePlayers().stream().map(
            player -> BetterHud.getInstance().getPlayerManager().getHudPlayer(player.getUniqueId()))
        .toList();

    for (HudPlayer onlinePlayer : hudPlayers) {
//      onlinePlayer.getHudObjects().add(hud);
    }
  }

  /**
   * On quit.
   *
   * @param event the event
   */
  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    Objects.requireNonNull(
            BetterHud.getInstance().getPlayerManager().getHudPlayer(event.getPlayer().getUniqueId()))
        .getHudObjects()
        .remove(getHud());
  }

  /**
   * Stop.
   */
  public void stop() {
    Hud hud = getHud();

    List<HudPlayer> hudPlayers = Bukkit.getOnlinePlayers().stream().map(
            player -> BetterHud.getInstance().getPlayerManager().getHudPlayer(player.getUniqueId()))
        .toList();

    for (HudPlayer onlinePlayer : hudPlayers) {
      onlinePlayer.getHudObjects().remove(hud);
    }

    HandlerList.unregisterAll(this);

    try {
      this.cancel();
    } catch (IllegalStateException ignore) {
    }
  }

  /**
   * Gets hud.
   *
   * @return the hud
   */
  private Hud getHud() {
    return BetterHud.getInstance().getHudManager().getHud("voting_hud");
  }

  /**
   * Gets current seconds.
   *
   * @return the current seconds
   */
  public int getCurrentSeconds() {
    return currentSeconds;
  }

  /**
   * Gets max seconds.
   *
   * @return the max seconds
   */
  public int getMaxSeconds() {
    return maxSeconds;
  }
}
