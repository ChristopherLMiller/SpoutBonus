package com.moosemanstudios.SpoutBonus;

import java.io.File;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public class SpoutBonus extends JavaPlugin {
	Logger log = Logger.getLogger("minecraft");	// console
	PluginDescriptionFile pdfFile = null;
	PluginManager pm = null;					// used to register events
	SBPlayerListener playerlistener = null;		// player listener
	Boolean debug = true;						// whether plugin in debug mode or not
	Boolean economyMode = false;				// economy for bonus
	double economyAmount;							// economy bonus amount
	Boolean itemMode = false;					// item for bonus
	int itemAmount;								// item bonus amount
	int itemType;								// item type for bonus
	Boolean vaultFound = false;					// if vault is found on the system
	Boolean economyFound = false;				// if economy plugin is found
	
	// vault stuff
	public static Economy economy = null;

	@Override
	public void onEnable() {
		// 1) see if spout is found, if not disable before we go anywhere!
		if (getServer().getPluginManager().getPlugin("Spout") == null) {
			log.info("[SpoutBonus] SpoutPlugin not found, disabling!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		// 2) go ahead and create config file if it doesn't exist
		File configFile = new File(getDataFolder() + File.separator + "config.yml");
		if (!configFile.exists()) {
			setupConfig();
		}
		
		// 3) see if Vault is found, if not we can't enable economy mode
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			// vault isn't found, so we can't use economy mode
			vaultFound = false;
		} else {
			// vault was found, setup economy if an economy plugin was found
			vaultFound = true;
			economyFound = setupEconomy();
		}
		
		// 4) at this point we have verified if spout, vault, and a economy plugin are found, go ahead and load config based on that info
		loadConfigFile();
		

		// 5) initialize the player listener
		playerlistener = new SBPlayerListener(this);
		
		// 6)register player join
		pm = this.getServer().getPluginManager();
		pm.registerEvents(playerlistener, this);
		
		
		// lastly inform that plugin is now enabled
		pdfFile = this.getDescription();
		log.info("[" + pdfFile.getName() + "] version " + pdfFile.getVersion() + " is now enabled");
	}
	
	@Override
	public void onDisable() {
		log.info("[SpoutBonus] is now disabled");
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		String[] split = args;
		String commandName = cmd.getName().toLowerCase();
		
		if (commandName.equalsIgnoreCase("spoutbonus")) {
			if (split.length == 0) {
				sender.sendMessage("Type " + ChatColor.RED + "/spoutbonus help" + ChatColor.WHITE + " for help");
			} else {
				if (split[0].equalsIgnoreCase("help")) {
					sender.sendMessage("SpoutBonus Help");
					sender.sendMessage("-------------------------");
					sender.sendMessage(ChatColor.RED + "/SpoutBonus help" + ChatColor.WHITE + ": Display this screen");
					sender.sendMessage(ChatColor.RED + "/SpoutBonus version " + ChatColor.WHITE + ": Display plugin version");
					
					if (sender.hasPermission("spoutbonus.admin")) {
						sender.sendMessage(ChatColor.RED + "/SpoutBonus reload" + ChatColor.WHITE + ": Reload SpoutBonus config");
					}
				} else if (split[0].equalsIgnoreCase("version")) {
					sender.sendMessage("SpoutBonus version: " + ChatColor.GOLD + pdfFile.getVersion() + ChatColor.WHITE + " Author: Moose517");
				} else if (split[0].equalsIgnoreCase("reload")) {
					if (sender.hasPermission("spoutbonus.admin")) {
						loadConfigFile();
						sender.sendMessage("SpoutBonus config reloaded");
					} else {
						sender.sendMessage("You don't have permissions to do that");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Unknown command, type " + ChatColor.WHITE + "/SpoutBonus help" + ChatColor.RED + " for help");
				}
			}
		}
		
		return false;
	}
	
	private void setupConfig() {
		getConfig().options().copyHeader(true);
		getConfig().options().copyDefaults(true);
		saveConfig();
		log.info("[SpoutBonus] config created");
	}
	
	private void loadConfigFile() {
		reloadConfig();
		
		debug = getConfig().getBoolean("debug");
		if (debug) {
			log.info("[SpoutBonus] debug mode enabled");
		}
		
		// see if vault was found
		if (vaultFound) {
			// yup, so see if they have economy mode enabled
			economyMode = getConfig().getBoolean("economy-bonus");
			if (economyMode) {
				// economy mode enabled, now see if they an economy plugin
				if (economyFound) {
					// economy plugin found, go ahead and load the values
					economyAmount = getConfig().getDouble("economy-bonus");
					log.info("[SpoutBonus] Economy mode enabled");
				} else {
					// no economy plugin found, alert user
					economyMode = false;
					economyAmount = 0;
					log.info("[SpoutBonus] Economy mode specified but no economy plugin found, reverting to item mode if enabled");
				}
			}
		} else {
			// see if they wanted economy mode, and alert if so
			if (getConfig().getBoolean("economy-bonus")) {
				log.info("[SpoutBonus] Economy mode specified but vault wasn't found, reverting to item mode if enabled");
				economyMode = false;
				economyAmount = 0;
			}
		}
		
		// load up item mode info
		itemMode = getConfig().getBoolean("item-bonus");
		if (itemMode) {
			itemType = getConfig().getInt("item-type");
			itemAmount = getConfig().getInt("item-amount");
			log.info("[SpoutBonus] Item mode enabled");
		}
		
		log.info("[SpoutBonus] config loaded");
	}
	
	private Boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}
		return (economy != null);
	}
}
