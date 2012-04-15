package com.moosemanstudios.SpoutBonus;

import org.bukkit.ChatColor;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;

public class SpoutCommandExecutorSpout {
	SpoutBonusSpout plugin;
	
	SpoutCommandExecutorSpout(SpoutBonusSpout instance) {
		plugin = instance;
	}
	
	@Command(desc="All SpoutBonus related commands", aliases="sb")
	public void spoutbonus(CommandContext args, CommandSource source) {
		String[] split = args.getRawArgs();
		String commandName = args.getCommand().toLowerCase();
		
		if (commandName.equalsIgnoreCase("spoutbonus")) {
			if (split.length == 0) {
				source.sendMessage("Type " + ChatColor.RED + "/spoutbonus help" + ChatColor.WHITE + " for help");
			} else {
				if (split[0].equalsIgnoreCase("help")) {
					source.sendMessage("SpoutBonus Help");
					source.sendMessage("-------------------------");
					source.sendMessage(ChatColor.RED + "/SpoutBonus help" + ChatColor.WHITE + ": Display this screen");
					source.sendMessage(ChatColor.RED + "/SpoutBonus version " + ChatColor.WHITE + ": Display plugin version");
					
					if (source.hasPermission("spoutbonus.admin")) {
						source.sendMessage(ChatColor.RED + "/SpoutBonus reload" + ChatColor.WHITE + ": Reload SpoutBonus config");
				}
				} else if (split[0].equalsIgnoreCase("version")) {
					source.sendMessage("SpoutBonus version: " + ChatColor.GOLD + plugin.pdfFile.getVersion() + ChatColor.WHITE + " Author: Moose517");
				} else if (split[0].equalsIgnoreCase("reload")) {
					if (source.hasPermission("spoutbonus.admin")) {
						plugin.loadConfigFile();
						source.sendMessage("SpoutBonus config reloaded");
					} else {
						source.sendMessage("You don't have permissions to do that");
					}
				} else {
					source.sendMessage(ChatColor.RED + "Unknown command, type " + ChatColor.WHITE + "/SpoutBonus help" + ChatColor.RED + " for help");
				}
			}
		}
	}
}
