package de.castcrafter.lootdrop.timer;

import de.castcrafter.lootdrop.Main;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;

/**
 * The type Loot drop timer.
 */
public class LootDropTimer extends BukkitRunnable {

	private static final ComponentLogger LOGGER = ComponentLogger.logger(LootDropTimer.class);

	private final long maxSeconds;
	private long seconds;

	/**
	 * Instantiates a new Loot drop timer.
	 *
	 * @param duration the duration
	 */
	public LootDropTimer(Duration duration) {
		this.maxSeconds = duration.getSeconds();
		this.seconds = this.maxSeconds + 1;
	}

	@Override
	public void run() {
		seconds--;

		if (seconds <= 0) {
			this.stop();
		}
	}

	/**
	 * Stop.
	 */
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

	/**
	 * Start.
	 */
	public void start() {
		if (this.isRunning()) {
			LOGGER.error("Could not start timer, already running");
			return;
		}

		this.runTaskTimer(Main.getInstance(), 0, 20L);
	}

	/**
	 * Gets seconds.
	 *
	 * @return the seconds
	 */
	public long getSeconds() {
		return seconds;
	}

	/**
	 * Gets max seconds.
	 *
	 * @return the max seconds
	 */
	public long getMaxSeconds() {
		return maxSeconds;
	}

	/**
	 * Is running boolean.
	 *
	 * @return the boolean
	 */
	public boolean isRunning() {
		try {
			return !this.isCancelled();
		} catch (Exception ignored) {
			return false;
		}
	}
}
