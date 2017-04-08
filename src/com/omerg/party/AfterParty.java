package com.omerg.party;

import org.bukkit.plugin.java.JavaPlugin;

public class AfterParty extends JavaPlugin
{
	
	private PartyManager partyManager;
	@Override
	public void onEnable()
	{
		// TODO Auto-generated method stub
		partyManager = new PartyManager(this);
		this.getCommand("party").setExecutor(new CommandManager(this));
		loadPartys();
	}

	private void loadPartys()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisable()
	{
		// TODO Auto-generated method stub
		savePartys();
	}

	private void savePartys()
	{
		// TODO Auto-generated method stub
		
	}

	public PartyManager getPartyManager() {
		return partyManager;
	}
}
