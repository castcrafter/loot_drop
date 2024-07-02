package de.castcrafter.lootdrop.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * The type Loot drop placeholder expansion.
 */
public class LootDropPlaceholderExpansion extends PlaceholderExpansion {

	@Override
	public @NotNull String getAuthor() {
		return "Ammo";
	}

	@Override
	public @NotNull String getIdentifier() {
		return "lootdrop";
	}

	@Override
	public @NotNull String getVersion() {
		return "1.0.0";
	}

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
		if (player == null) {
			return null;
		}

		String[] paramsArray = params.split("_");

		if (paramsArray.length == 0) {
			return null;
		}

		if (paramsArray[ 0 ].equalsIgnoreCase("name") && paramsArray.length >= 2) {
			return Arrays.stream(Arrays.copyOfRange(paramsArray, 1, paramsArray.length)).reduce((a, b) -> a + "_" + b)
						 .orElse("");
		}

		return null;
	}
}
