package com.moosemanstudios.SpoutBonus;

import java.io.File;
import java.util.logging.Logger;

import org.spout.api.Engine;
import org.spout.api.event.EventManager;
import org.spout.api.plugin.CommonPlugin;
import org.spout.api.plugin.PluginDescriptionFile;
import org.spout.api.util.config.Configuration;
import org.spout.api.util.config.yaml.YamlConfiguration;
import org.spout.api.command.CommandRegistrationsFactory;
import org.spout.api.command.annotated.AnnotatedCommandRegistrationFactory;
import org.spout.api.command.annotated.SimpleAnnotatedCommandExecutorFactory;
import org.spout.api.command.annotated.SimpleInjector;

@SuppressWarnings("serial")
public class SpoutBonusSpout extends CommonPlugin {
	Logger log = Logger.getLogger("minecraft");
	String prefix = "[SpoutBonus] ";
	PluginDescriptionFile pdfFile = null;
	EventManager em = null;
	SBPlayerListenerSpout playerlistener = null;
	Boolean debug = true;
	Boolean itemMode = false;
	int itemAmount;
	int itemType;
	
	public void onDisable() {
		log.info(prefix + "is now disabled!");
	}
	
	public void onEnable() {
		
		Engine game = getGame();
		// create the config file
		setupConfig();
		
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
		
		// register command executor
		CommandRegistrationsFactory<Class<?>> commandRegFactory = new AnnotatedCommandRegistrationFactory(new SimpleInjector(this), new SimpleAnnotatedCommandExecutorFactory());
		game.getRootCommand().addSubCommands(game, SpoutCommandExecutorSpout.class, commandRegFactory);
		
		// finally inform that plugin is enabled
		pdfFile = this.getDescription();
		log.info(prefix + "version " + pdfFile.getVersion() + " is now enabled");
		
	}

	public void loadConfigFile() {
		// TODO Auto-generated method stub
		
	}

	public void setupConfig() {
		@SuppressWarnings("unused")
		Configuration config = new YamlConfiguration(new File(this.getDataFolder() + File.separator + "config.yml"));
		
	}
	
	

	
}
