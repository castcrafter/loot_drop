package org.example.cast.untitled;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * The type Messages.
 */
public class Messages {

	/**
	 * No event started component component.
	 *
	 * @return the component
	 */
	public static Component noEventStartedComponent() {
		return Component.text("Derzeit findet kein Event statt.", NamedTextColor.RED);
	}

	/**
	 * Only players can execute command component component.
	 *
	 * @return the component
	 */
	public static Component onlyPlayersCanExecuteCommandComponent() {
		return Component.text("Nur Spieler können diesen Befehl ausführen.", NamedTextColor.RED);
	}

	/**
	 * No permission component component.
	 *
	 * @return the component
	 */
	public static Component noPermissionComponent() {
		return Component.text("Du besitzt keine Rechte, um diesen Befehl auszuführen.", NamedTextColor.RED);
	}

	/**
	 * You have been teleported to event location component component.
	 *
	 * @return the component
	 */
	public static Component youHaveBeenTeleportedToEventLocationComponent() {
		return Component.text("Du wurdest zum Event teleportiert.", NamedTextColor.GREEN);
	}

	/**
	 * Event has been started component component.
	 *
	 * @return the component
	 */
	public static Component eventHasBeenStartedComponent() {
		TextComponent.Builder builder = Component.text();

		builder.appendNewline();
		builder.append(Component.text("Ein neues Event wurde gestartet! ", NamedTextColor.GREEN));
		builder.append(Component.text("\u2320", NamedTextColor.GREEN)
								.clickEvent(ClickEvent.runCommand("/join"))
								.hoverEvent(Component.text("Klicke um am Event teilzunehmen.")));
		builder.appendNewline();

		return builder.build();
	}

	/**
	 * Event has been created component component.
	 *
	 * @return the component
	 */
	public static Component eventHasBeenCreatedComponent() {
		return Component.text("Das Event wurde an deiner Position erstellt.", NamedTextColor.GREEN);
	}

	/**
	 * Event has been stopped component component.
	 *
	 * @return the component
	 */
	public static Component eventHasBeenStoppedComponent() {
		return Component.text("Das Event wurde beendet.", NamedTextColor.GREEN);
	}
}