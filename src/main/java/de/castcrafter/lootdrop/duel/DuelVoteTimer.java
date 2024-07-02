package de.castcrafter.lootdrop.duel;

import de.castcrafter.lootdrop.Main;
import de.castcrafter.lootdrop.utils.SoundUtils;
import de.castcrafter.lootdrop.utils.TimeUtils;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * The type Duel vote timer.
 */
public class DuelVoteTimer extends BukkitRunnable {

	private final int[] plingSounds = new int[] {
			120, 90, 60, 30, 10, 5, 4, 3, 2, 1
	};

	private final Duel duel;
	private final int maxSeconds;
	private int currentSeconds;

	private BossBar bossBar;

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

		updateBossBar();
		updateActionBar();
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
	 * Update action bar.
	 */
	public void updateActionBar() {
		Bukkit.getOnlinePlayers().forEach(player -> {
			player.sendActionBar(Component.text("Duell-Abstimmung: ", NamedTextColor.GOLD)
										  .append(TimeUtils.formatSeconds(currentSeconds)));
		});
	}

	/**
	 * Update boss bar.
	 */
	public void updateBossBar() {
		Player playerOne = duel.getPlayerOne();
		Player playerTwo = duel.getPlayerTwo();

		int playerOnePercentage = duel.getVotesPercentage(playerOne);
		int playerTwoPercentage = 100 - playerOnePercentage;

		String playerOneString = playerOne.getName() + " (" + playerOnePercentage + "%)";
		String playerTwoString = playerTwo.getName() + " (" + playerTwoPercentage + "%)";

		if (this.bossBar == null) {
			this.bossBar =
					BossBar.bossBar(Component.text(playerOneString + " vs " + playerTwoString), 1,
									BossBar.Color.GREEN, BossBar.Overlay.PROGRESS
					);
		}

		this.bossBar.name(Component.text(playerOneString + " vs " + playerTwoString));
		this.bossBar.progress((float) playerOnePercentage / 100);

		Bukkit.getOnlinePlayers().forEach(player -> {
			player.showBossBar(this.bossBar);
		});
	}

	/**
	 * Start.
	 */
	public void start() {
		this.runTaskTimer(Main.getInstance(), 0, 20L);
	}

	/**
	 * Stop.
	 */
	public void stop() {
		if (this.bossBar != null) {
			Bukkit.getOnlinePlayers().forEach(player -> {
				player.hideBossBar(this.bossBar);
			});
		}

		try {
			this.cancel();
		} catch (IllegalStateException ignore) {
		}
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
