package com.moosemanstudios.SpoutBonus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.player.SpoutPlayer;

public class SBPlayerListener implements Listener {
	private SpoutBonus plugin;
	
	SBPlayerListener(SpoutBonus instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onSpoutCraftEnable(SpoutCraftEnableEvent event) {
		String date = new SimpleDateFormat("yy/MM/dd").format(Calendar.getInstance().getTime());
		SpoutPlayer splayer = event.getPlayer();
		String lastLogin;
		
		// see if they have ever logged in before
		if (!plugin.getConfig().contains(splayer.getName())) {
			lastLogin = "00/00/00";
		} else {
			lastLogin = plugin.getConfig().getString(splayer.getName());
		}
		
		// see if the last login was the same as today's date
		if (!lastLogin.equalsIgnoreCase(date)) {
			// player hasn't logged in yet today with spoutcraft, give them a bonus
			splayer.sendMessage(ChatColor.AQUA + "You have recieved a bonus for using spoutcraft today!");
			
			if (plugin.economyMode) {
				SpoutBonus.economy.depositPlayer(event.getPlayer().getName(), plugin.economyAmount);
				splayer.sendMessage(ChatColor.AQUA + Double.toString(plugin.economyAmount) + " has been credited to you");
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
			plugin.getConfig().set(splayer.getName(), date);
			plugin.saveConfig();
			
			// tell all the other players they received it
			for (Player player : plugin.getServer().getOnlinePlayers()) {
				// make sure we don't tell ourselves about it
				if (!splayer.getName().equalsIgnoreCase(player.getName())) {
					player.sendMessage(ChatColor.AQUA + splayer.getName() + " has recieved a bonus for using spoutcraft client!");
				}
			}
		}
	}
}
