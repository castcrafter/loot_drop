package de.castcrafter.lootdrop.duel;

import de.castcrafter.lootdrop.Main;
import de.castcrafter.lootdrop.utils.SoundUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Duel.
 */
public class Duel {

	private final Player playerOne;
	private final Player playerTwo;

	private final Map<Player, Player> playerVotes;
	private boolean voteOpen;

	private final DuelGui duelGui;
	private final List<ItemStack> rewards;

	private final DuelVoteTimer voteTimer;

	/**
	 * Instantiates a new Duel.
	 *
	 * @param playerOne    the player one
	 * @param playerTwo    the player two
	 * @param rewards      the rewards
	 * @param voteDuration the vote duration
	 */
	public Duel(Player playerOne, Player playerTwo, List<ItemStack> rewards, int voteDuration) {
		this.playerOne = playerOne;
		this.playerTwo = playerTwo;

		this.voteOpen = true;
		this.playerVotes = new HashMap<>();

		this.duelGui = new DuelGui(this);
		this.rewards = rewards;

		this.voteTimer = new DuelVoteTimer(this, voteDuration);
		this.voteTimer.start();
	}

	/**
	 * End vote.
	 */
	public void endVote() {
		this.voteOpen = false;
	}

	/**
	 * Add vote duel vote state.
	 *
	 * @param voter  the voter
	 * @param player the player
	 *
	 * @return the duel vote state
	 */
	public DuelVoteState addVote(Player voter, Player player) {
		if (voter.equals(player)) {
			return DuelVoteState.CANNOT_VOTE_SELF;
		}

		if (!voteOpen) {
			return DuelVoteState.VOTE_CLOSED;
		}

		if (playerVotes.containsKey(voter)) {
			return DuelVoteState.ALREADY_VOTED;
		}

		playerVotes.put(voter, player);

		return DuelVoteState.SUCCESS;
	}

	/**
	 * Gets votes.
	 *
	 * @param player the player
	 *
	 * @return the votes
	 */
	public int getVotes(Player player) {
		return (int) playerVotes.values().stream().filter(player::equals).count();
	}

	/**
	 * Gets votes percentage.
	 *
	 * @param player the player
	 *
	 * @return the votes percentage
	 */
	public int getVotesPercentage(Player player) {
		return (int) ( (double) playerVotes.values().stream().filter(player::equals).count() / playerVotes.size() *
					   100 );
	}

	/**
	 * Gets player one.
	 *
	 * @return the player one
	 */
	public Player getPlayerOne() {
		return playerOne;
	}

	/**
	 * Gets player two.
	 *
	 * @return the player two
	 */
	public Player getPlayerTwo() {
		return playerTwo;
	}

	/**
	 * Is vote open boolean.
	 *
	 * @return the boolean
	 */
	public boolean isVoteOpen() {
		return voteOpen;
	}

	/**
	 * Gets duel gui.
	 *
	 * @return the duel gui
	 */
	public DuelGui getDuelGui() {
		return duelGui;
	}

	/**
	 * Open duel gui.
	 *
	 * @param playSound the play sound
	 */
	public void openDuelGui(boolean playSound) {
		Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
			duelGui.show(onlinePlayer);

			if (playSound) {
				SoundUtils.playSound(onlinePlayer, org.bukkit.Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5f, 0.75f);
			}
		});
	}

	/**
	 * Finish duel.
	 *
	 * @param winningPlayer the winning player
	 */
	public void finishDuel(Player winningPlayer) {
		Player losingPlayer = winningPlayer.equals(playerOne) ? playerTwo : playerOne;

		int winningPlayerVotes = (int) playerVotes.values().stream().filter(winningPlayer::equals).count();
		int losingPlayerVotes = playerVotes.size() - winningPlayerVotes;

		int winningPlayerVotesPercentage = (int) ( (double) winningPlayerVotes / playerVotes.size() * 100 );
		int losingPlayerVotesPercentage = 100 - winningPlayerVotesPercentage;

		Component titleComponent = Component.text(winningPlayer.getName(), NamedTextColor.GOLD, TextDecoration.BOLD);
		Component subtitleComponent = Component.text("hat das Duell gewonnen", NamedTextColor.GOLD);
		Title.Times times = Title.Times.times(Duration.ofMillis(150), Duration.ofSeconds(4), Duration.ofMillis(150));
		Title title = Title.title(titleComponent, subtitleComponent, times);

		Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
			duelGui.closeInventory(onlinePlayer);

			onlinePlayer.sendMessage(Component.empty());
			onlinePlayer.sendMessage(Component.text("-------------------------------", NamedTextColor.GRAY));
			onlinePlayer.sendMessage(Component.empty());
			onlinePlayer.sendMessage(Component.text("Das Duell wurde beendet", NamedTextColor.GOLD));
			onlinePlayer.sendMessage(Component.empty());

			onlinePlayer.sendMessage(
					Component.text(winningPlayer.getName() + " hat das Duell gewonnen.", NamedTextColor.GREEN));
			onlinePlayer.sendMessage(Component.text("Abgegebene Stimmen:", NamedTextColor.GRAY));
			onlinePlayer.sendMessage(Component.text(" - " + winningPlayer.getName() + ": " + winningPlayerVotes + " (" +
													winningPlayerVotesPercentage + "%)", NamedTextColor.GREEN));
			onlinePlayer.sendMessage(Component.text(" - " + losingPlayer.getName() + ": " + losingPlayerVotes + " (" +
													losingPlayerVotesPercentage + "%)", NamedTextColor.RED));

			onlinePlayer.sendMessage(Component.empty());
			onlinePlayer.sendMessage(Component.text("-------------------------------", NamedTextColor.GRAY));
			onlinePlayer.sendMessage(Component.empty());

			SoundUtils.playSound(onlinePlayer, org.bukkit.Sound.ENTITY_ENDER_DRAGON_GROWL, .5f, 1);
			onlinePlayer.showTitle(title);
		});

		new BukkitRunnable() {
			@Override
			public void run() {
				List<Player> playersVotedFor = playerVotes.entrySet().stream()
														  .filter(entry -> entry.getValue().equals(winningPlayer))
														  .map(Map.Entry::getKey)
														  .toList();

				playersVotedFor.forEach(player -> {
					if (player == null || !player.isOnline()) {
						return;
					}

					rewards.forEach(reward -> {
						HashMap<Integer, ItemStack> notAddedItems = player.getInventory().addItem(reward);

						if (!notAddedItems.isEmpty()) {
							notAddedItems.values()
										 .forEach(itemStack -> player.getWorld()
																	 .dropItem(player.getLocation(), itemStack,
																			   item -> {
																				   item.setCanMobPickup(false);
																				   item.setCanPlayerPickup(true);
																				   item.setOwner(player.getUniqueId());
																				   item.setUnlimitedLifetime(true);
																			   }
																	 ));

							int totalDroppedCount =
									notAddedItems.values().stream().mapToInt(ItemStack::getAmount).sum();

							player.sendMessage(Component.text(
																"Dein Inventar war voll, daher wurden ",
																NamedTextColor.RED
														).append(Component.text(
																totalDroppedCount + " Item" + ( totalDroppedCount == 1 ? "" : "s" ),
																NamedTextColor.YELLOW
														))
														.append(Component.text(
																" auf den Boden geworfen. Nur du kannst diese" +
																( totalDroppedCount == 1 ? "s" : "" ) +
																" Item " + ( totalDroppedCount == 1 ? "" : "s" ) +
																"einsammeln.",
																NamedTextColor.RED
														)));
						}
					});

					player.sendMessage(Component.text("Dein Gewinn wurde dir zugestellt.", NamedTextColor.GREEN));
				});
			}
		}.runTaskLater(Main.getInstance(), 20 * 4);
	}

	/**
	 * Gets rewards.
	 *
	 * @return the rewards
	 */
	public List<ItemStack> getRewards() {
		return rewards;
	}

	/**
	 * Gets vote timer.
	 *
	 * @return the vote timer
	 */
	public DuelVoteTimer getVoteTimer() {
		return voteTimer;
	}

	/**
	 * Gets player votes.
	 *
	 * @return the player votes
	 */
	public Map<Player, Player> getPlayerVotes() {
		return playerVotes;
	}
}
