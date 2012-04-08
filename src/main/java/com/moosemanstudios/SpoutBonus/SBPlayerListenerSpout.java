package com.moosemanstudios.SpoutBonus;

import org.getspout.api.event.Listener;

public class SBPlayerListenerSpout implements Listener{
	@SuppressWarnings("unused")
	private SpoutBonusSpout plugin;
	
	SBPlayerListenerSpout(SpoutBonusSpout instance) {
		plugin = instance;
	}
}
