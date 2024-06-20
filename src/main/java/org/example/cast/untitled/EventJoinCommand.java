package org.example.cast.untitled;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The type Event join command.
 */
public class EventJoinCommand implements CommandExecutor {

    private Location eventLocation;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.onlyPlayersCanExecuteCommandComponent());
            return true;
        }

        String subCommand = args[0];

        if (subCommand.equalsIgnoreCase("create")) {
            if (!player.hasPermission("event.create")) {
                player.sendMessage(Messages.noPermissionComponent());
                return true;
            }

            this.eventLocation = player.getLocation();
            player.sendMessage(Messages.eventHasBeenCreatedComponent());
        } else if (subCommand.equalsIgnoreCase("start")) {
            if (!player.hasPermission("event.start")) {
                player.sendMessage(Messages.noPermissionComponent());
                return true;
            }

            if (eventLocation == null) {
                player.sendMessage(Messages.noEventStartedComponent());
                return true;
            }

            Bukkit.broadcast(Messages.eventHasBeenStartedComponent());

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);
            }
        } else if (subCommand.equalsIgnoreCase("stop")) {
            if (!player.hasPermission("event.stop")) {
                player.sendMessage(Messages.noPermissionComponent());
                return true;
            }

            Bukkit.broadcast(Messages.eventHasBeenStoppedComponent());

            this.eventLocation = null;
        }

        return true;
    }

    /**
     * Perform join sub command boolean.
     *
     * @param player the player
     *
     * @return the boolean
     */
    public boolean performJoinSubCommand(Player player) {
        if (eventLocation == null) {
            player.sendMessage(Messages.noEventStartedComponent());
            return true;
        }

        player.teleportAsync(eventLocation);
        player.sendMessage(Messages.youHaveBeenTeleportedToEventLocationComponent());

        return true;
    }
}