package de.castcrafter.lootdrop.command.commands.lootdrop.subcommands;

import de.castcrafter.lootdrop.config.LootDropConfig;
import de.castcrafter.lootdrop.timer.LootDropTimer;
import de.castcrafter.lootdrop.utils.Chat;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class LootDropStartCommand extends CommandAPICommand {

	public LootDropStartCommand(String commandName) {
		super(commandName);

		withPermission("lootdrop.command.lootdrop.start");

		withArguments(new IntegerArgument("year"));
		withArguments(new IntegerArgument("month"));
		withArguments(new IntegerArgument("day"));
		withArguments(new IntegerArgument("hour"));
		withArguments(new IntegerArgument("minute"));

		executesPlayer((player, args) -> {
			LootDropConfig config = LootDropConfig.INSTANCE;
			LootDropTimer oldTimer = config.getTimer();

			if (oldTimer != null && oldTimer.isRunning()) {
				Chat.sendMessage(player, Component.text("Es läuft bereits ein Timer!", NamedTextColor.RED));
				return;
			}

			int year = args.getOrDefaultUnchecked("year", 2024);
			int month = args.getOrDefaultUnchecked("month", 1);
			int day = args.getOrDefaultUnchecked("day", 1);
			int hour = args.getOrDefaultUnchecked("hour", 0);
			int minute = args.getOrDefaultUnchecked("minute", 0);

			ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
			ZonedDateTime endTime =
					ZonedDateTime.of(year, month, day, hour, minute, 0, 0, ZoneId.systemDefault());

			config.setStartTimestamp(now);
			config.setEndTimestamp(endTime);

			long diff = endTime.toEpochSecond() - now.toEpochSecond();

			LootDropTimer timer = new LootDropTimer(Duration.ofSeconds(diff));
			timer.start();
			
			config.setTimer(timer);
			config.saveConfig();
		});
	}
}
