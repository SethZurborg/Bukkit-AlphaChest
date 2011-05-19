package com.mast3rplan.alphachest.commands;

import net.minecraft.server.EntityPlayer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mast3rplan.alphachest.AlphaChestManager;
import com.mast3rplan.alphachest.AlphaChestPlugin;
import com.mast3rplan.alphachest.Teller;
import com.mast3rplan.alphachest.Teller.Type;

public class ChestCommands implements CommandExecutor {

	private final AlphaChestPlugin plugin;
	private final AlphaChestManager chestManager;

	public ChestCommands(AlphaChestPlugin plugin, AlphaChestManager chestManager) {
		this.plugin = plugin;
		this.chestManager = chestManager;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		final String name = command.getName();
		if (name.equals("chest"))
			return performChestCommand(sender, args);
		else if (name.equalsIgnoreCase("clearchest"))
			return performClearChestCommand(sender, args);
		else if (name.equalsIgnoreCase("savechests"))
			return performSaveChestsCommand(sender, args);
		else
			return false;
	}

	
	private boolean performChestCommand(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			EntityPlayer eh;
			if (args.length == 1) {
				if (plugin.hasPermission(player, "ac.admin")) {
					eh = ((CraftPlayer) sender).getHandle();
					eh.a(chestManager.getChest(args[0]));
				} else {
					Teller.tell(player, Type.Warning, "You\'re not allowed to use this command.");
				}
				return true;
	
			} else if (args.length == 0) {
				if (plugin.hasPermission(player, "ac.chest")) {
					eh = ((CraftPlayer) sender).getHandle();
					eh.a(chestManager.getChest(player.getName()));
				} else {
					Teller.tell(player, Type.Warning, "You\'re not allowed to use this command.");
				}
				return true;
			}
		}
		return false;
	}

	private boolean performClearChestCommand(CommandSender sender, String[] args) {
		if (args.length >= 1) {
			if ((sender instanceof Player) && !plugin.hasPermission((Player) sender, "ac.admin")) {
				Teller.tell(sender, Type.Warning, "You\'re not allowed to clear other user's chests.");
				return true;
			}
			chestManager.removeChest(args[0]);
			Teller.tell(sender, Type.Success, "Successfully cleared " + args[0] + "\'s chest.");
			return true;
		} else {
			if (sender instanceof Player) {
				final Player player = (Player) sender;
				if (!plugin.hasPermission(player, "ac.chest")) {
					Teller.tell(player, Type.Warning, "You\'re not allowed to use this command.");
				} else {
					chestManager.removeChest(player.getName());
					Teller.tell(player, Type.Success, "Successfully cleared your chest.");
				}
				return true;
			}
		}
		return false;
	}

	private boolean performSaveChestsCommand(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			if (!plugin.hasPermission((Player) sender, "ac.save")) {
				Teller.tell(sender, Type.Warning, "You\'re not allowed to use this command.");
				return true;
			}
		}

		chestManager.save();
		Teller.tell(sender, Type.Success, "Saved all chests.");
		return true;
	}

}