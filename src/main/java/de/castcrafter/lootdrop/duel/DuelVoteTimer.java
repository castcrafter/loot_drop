package de.castcrafter.lootdrop.duel;

import de.castcrafter.lootdrop.Main;
import de.castcrafter.lootdrop.utils.TimeUtils;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * The type Duel vote timer.
 */
public class DuelVoteTimer extends BukkitRunnable {

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

		this.currentSeconds = maxSeconds;
		this.maxSeconds = maxSeconds;
	}

	@Override
	public void run() {
		if (currentSeconds <= 0) {
			duel.endVote();
			stop();
			return;
		}

		updateBossBar();
		updateActionBar();

		currentSeconds--;
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

		if (this.bossBar == null) {
			this.bossBar =
					BossBar.bossBar(Component.text(playerOne.getName() + " vs " + playerTwo.getName()), 1,
									BossBar.Color.GREEN, BossBar.Overlay.PROGRESS
					);
		}

		this.bossBar.progress((float) playerOnePercentage / 100);
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
