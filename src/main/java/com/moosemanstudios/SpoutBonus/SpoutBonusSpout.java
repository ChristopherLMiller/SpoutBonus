package com.moosemanstudios.SpoutBonus;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.getspout.api.event.EventManager;
import org.getspout.api.plugin.CommonPlugin;
import org.getspout.api.plugin.PluginDescriptionFile;

public class SpoutBonusSpout extends CommonPlugin {
	Logger log = Logger.getLogger("minecraft");
	String prefix = "[SpoutBonus] ";
	PluginDescriptionFile pdfFile = null;
	EventManager em = null;
	SBPlayerListenerSpout playerlistener = null;
	Boolean debug = true;
	Boolean economyMode = false;
	double economyAmount;
	Boolean itemMode = false;
	int itemAmount;
	int itemType;
	Boolean vaultFound = false;
	Boolean economyFound = false;
	
	// vault stuff
	// TODO: can't do yet
	public static Economy economy = null;
	
	public void onDisable() {
		log.info(prefix + "is now disabled!");
		
	}
	
	public void onEnable() {
		// create the config file
		setupConfig();
		
		// See if vault is found, if not we can't enable economy mode
		if (getGame().getPluginManager().getPlugin("Vault") == null) {
			// vault isn't found, can't use economy mode
			vaultFound = false;
		} else {
			vaultFound = true;
			economyFound = setupEconomy();
		}
		
		// load config
		loadConfigFile();
		
		// initalize the player listener
		playerlistener = new SBPlayerListenerSpout(this);
		
		// register player join
		em = getGame().getEventManager();
		em.registerEvents(playerlistener, this);
		
		// enable metrics tracking
		// TODO: not spoutplugin ready yet
		/*try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		// finally inform that plugin is enabled
		pdfFile = this.getDescription();
		log.info(prefix + "version " + pdfFile.getVersion() + " is now enabled");
		
	}

	private Boolean setupEconomy() {
		// TODO Auto-generated method stub
		return null;
	}

	private void loadConfigFile() {
		// TODO Auto-generated method stub
		
	}

	private void setupConfig() {
		// TODO Auto-generated method stub
		
	}
	
	

	
}
