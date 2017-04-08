package com.omerg.party;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;

public class PartyManager {

	private AfterParty plugin;
	private List<Party> parties = new ArrayList<Party>();
	
	public PartyManager(AfterParty plugin) {
		this.plugin = plugin;
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable() {
			
			@Override
			public void run() {
				parties.forEach(Party::checkExpiredInvitations);
			}
		}, 20L, 20L);
	}

	public Party createParty(UUID leader) {
		if (getPartyByMember(leader) == null) {
			Party party = new Party(this, leader);
			MessageSender.sendSuccssesMessage(leader, "You created a new party!");
			return party;
		} else {
			MessageSender.sendErrorMessage(leader, "Error: Please leave your party to create a new one!");
			return null;
		}
	}

	public Party getPartyByLeader(UUID leader) {
		for (Party par : parties)
			if (par.getLeader().getPlayerID().equals(leader))
				return par;
		return null;
	}
	
	public Party getPartyByLeaderName(String name) {
		for (Party par : parties)
			if (Bukkit.getOfflinePlayer(par.getLeader().getPlayerID()).getName().equalsIgnoreCase(name))
				return par;
		return null;
	}

	public Party getPartyByMember(UUID member) {
		for (Party par : parties)
			if (par.hasMember(member))
				return par;
		return null;
	}
	
	protected void removeParty(Party p) {
		parties.remove(p);
	}

}
