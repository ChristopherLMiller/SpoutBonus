package com.moosemanstudios.SpoutBonus;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.player.SpoutPlayer;

public class SBPlayerListenerBukkit implements Listener {
	private SpoutBonusBukkit plugin;
	public FileConfiguration lastPlayed = null;
	public File lastPlayedFile = null;
	
	SBPlayerListenerBukkit(SpoutBonusBukkit instance) {
		plugin = instance;
		
		if (lastPlayedFile == null) {
			lastPlayedFile = new File(plugin.getDataFolder(), "lastplayed.yml");
		}
		lastPlayed = YamlConfiguration.loadConfiguration(lastPlayedFile);
	}
	
	@EventHandler
	public void onSpoutCraftEnable(SpoutCraftEnableEvent event) {
		String date = new SimpleDateFormat("yy/MM/dd").format(Calendar.getInstance().getTime());
		SpoutPlayer splayer = event.getPlayer();
		String lastLogin;
		
		if (!plugin.spoutPlayers.contains(event.getPlayer().getName())) {
			plugin.spoutPlayers.add(event.getPlayer().getName());
		}
		
		// see if they have ever logged in before
		if (!lastPlayed.contains(splayer.getName())) {
			lastLogin = "00/00/00";
		} else {
			lastLogin = lastPlayed.getString(splayer.getName());
		}
		
		// see if the last login was the same as today's date
		if (!lastLogin.equalsIgnoreCase(date)) {
			// player hasn't logged in yet today with spoutcraft, give them a bonus
			splayer.sendMessage(ChatColor.AQUA + "You have received a bonus for using spoutcraft today!");
			
			if (plugin.economyMode) {
				if (SpoutBonusBukkit.economy.depositPlayer(event.getPlayer().getName(), plugin.economyAmount).transactionSuccess()) {
					splayer.sendMessage(ChatColor.AQUA + Double.toString(plugin.economyAmount) + " has been credited to you");
				} else {
					splayer.sendMessage(ChatColor.RED + "Failed to credit your account.");
				}

			}
			if (plugin.itemMode) {
				HashMap<Integer, ItemStack> leftOver = splayer.getInventory().addItem(new ItemStack(plugin.itemType, plugin.itemAmount));
				
				if (!leftOver.isEmpty()) {
					// loop through the hashmap to drop anything
					for (Integer key : leftOver.keySet()) {
						splayer.getWorld().dropItemNaturally(splayer.getLocation(), leftOver.get(key));
						splayer.sendMessage(ChatColor.AQUA + "Your inventory is full, check the ground for it");
					}
				} else {
					splayer.sendMessage(ChatColor.AQUA + "Check your inventory for your bonus");
				}
			}
			
			// at this point update the date in the config file with todays so they can't claim multiple times
			try {
				lastPlayed.set(splayer.getName(), date);
				lastPlayed.save(lastPlayedFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// tell all the other players they received it
			for (Player player : plugin.getServer().getOnlinePlayers()) {
				// make sure we don't tell ourselves about it
				if (!splayer.getName().equalsIgnoreCase(player.getName())) {
					player.sendMessage(ChatColor.AQUA + splayer.getName() + " has received a bonus for using spoutcraft client!");
				}
			}
		}
	}
}
