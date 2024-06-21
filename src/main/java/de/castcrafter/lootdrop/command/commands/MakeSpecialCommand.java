package de.castcrafter.lootdrop.command.commands;

import de.castcrafter.lootdrop.Messages;
import de.castcrafter.lootdrop.listener.listeners.SpecialItemListener;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

/**
 * The type Make special command.
 */
public class MakeSpecialCommand extends CommandAPICommand {

	/**
	 * Instantiates a new Make special command.
	 *
	 * @param commandName the command name
	 */
	public MakeSpecialCommand(String commandName) {
		super(commandName);

		withPermission("castcrafter.lootdrop.specialitem");

		executesPlayer((player, args) -> {
			ItemStack mainHand = player.getInventory().getItemInMainHand();

			if (mainHand.getType().equals(Material.AIR)) {
				player.sendMessage(Messages.youNeedAnItemInYourHandComponent());
				return;
			}

			ItemMeta itemMeta = mainHand.getItemMeta();

			if (itemMeta == null) {
				player.sendMessage(Messages.itemHasNoMetaComponent());
				return;
			}

			itemMeta.getPersistentDataContainer()
					.set(SpecialItemListener.SPECIAL_KEY, PersistentDataType.BYTE, (byte) 1);

			mainHand.setItemMeta(itemMeta);

			player.sendMessage(Messages.itemIsNowSpecialComponent());
		});
	}
}
