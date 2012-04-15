package com.moosemanstudios.SpoutBonus;

import org.spout.api.event.Listener;

public class SBPlayerListenerSpout implements Listener{
	@SuppressWarnings("unused")
	private SpoutBonusSpout plugin;
	
	SBPlayerListenerSpout(SpoutBonusSpout instance) {
		plugin = instance;
	}
}
