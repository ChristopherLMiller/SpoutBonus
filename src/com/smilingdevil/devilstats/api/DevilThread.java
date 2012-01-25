package com.smilingdevil.devilstats.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * 
 * @author SmilingDevil
 */
public class DevilThread implements Runnable {

	String _url = "";
	DevilStats hook;

	public DevilThread(String url, DevilStats hook) {
		this._url = url;
		this.hook = hook;

	}

	@Override
	public void run() {
		try {
			URL url = new URL(_url);
			URLConnection conn = url.openConnection();
			// Open input stream
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String inputLine;

			if ((inputLine = in.readLine()) != null)
				// Handle result
				parseResults(inputLine);
			// Close
			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void parseResults(String result) {
		if (result.equals("SUCCESS")) {
			// Success
			hook.log("Successfully logged startup for " + hook.getPlugin()
					+ " version " + hook.getVersion());
		} else if (result.equals("MISSING_INFO")) {
			// We're missing something
			hook.log("We seem to be missing some info from your plugin.yml!");
			hook.log("DEBUG VALUES - ");
			hook.log("Author: " + hook.values.get("author"));
			hook.log("Plugin: " + hook.values.get("name"));
			hook.log("Version: " + hook.values.get("version"));
			hook.log("PLEASE report this log to SmilingDevil in #devilstats in irc.esper.net");
		} else {
			// How did you even get here?
			// Other unhandled errors, possibly HTTP errors?
			hook.log("DevilStats couldn't handle the returned value from the server.");
			hook.log("Value: " + result);
		}
	}
}