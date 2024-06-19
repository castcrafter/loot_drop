package org.example.cast.untitled;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class event_join implements CommandExecutor {

    private Location eventLocation;
    private boolean eventStarted = false;


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl ausführen.");
            return true;
        }

        Player player = (Player) sender;
        String subCommand = args[0];

        if (subCommand.equalsIgnoreCase("create")) {
            if (!player.hasPermission("event.create")) {
                player.sendMessage("You do not have permission to use this command.");
                return true;
            }

            if (args.length < 2) {
                player.sendMessage("Bitte gib einen Namen für das Event an.");
                return true;
            }

            eventLocation = player.getLocation();
            player.sendMessage("Event " + args[1] + " wurde an deiner Position erstellt.");

        } else if (subCommand.equalsIgnoreCase("start")) {
            if (!player.hasPermission("event.start")) {
                player.sendMessage("You do not have permission to use this command.");
                return true;
            }
            if (eventLocation == null) {
                player.sendMessage("Es wurde noch kein Event erstellt.");
                return true;
            }

            eventStarted = true;
            Component message = Component.text("\n")
                    .append(Component.text("Ein neues Event hat gestartet! ")
                            .append(Component.text("\u2320")
                                    .color(NamedTextColor.GREEN)
                                    .clickEvent(ClickEvent.runCommand("/join"))
                                    .hoverEvent(Component.text("Klicke um am Event teilzunehmen."))))
                    .append(Component.text("\n"));

            Bukkit.getServer().sendMessage(message);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);
            }

        } else if (subCommand.equalsIgnoreCase("stop")) {
            if (!player.hasPermission("event.stop")) {
                player.sendMessage("You do not have permission to use this command.");
                return true;
            }

            eventStarted = false;
            eventLocation = null; // Delete the event location
            Bukkit.broadcastMessage("Das Event wurde gestoppt");

        }

        return true;
    }

    public boolean joinCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl ausführen.");
            return true;
        }

        Player player = (Player) sender;

        if (!eventStarted) {
            player.sendMessage("Derzeit gibt es kein Event.");
            return true;
        }

        if (eventLocation == null) {
            player.sendMessage("Es gibt noch keine Event location.");
            return true;
        }

        player.teleport(eventLocation);
        player.sendMessage("Du wurdest zum Event teleportiert.");

        return true;
    }
}