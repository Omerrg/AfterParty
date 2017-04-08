package com.omerg.party;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;

import net.md_5.bungee.api.ChatColor;

public class Party
{
	private PartyManager manager;
	
	private boolean isOpen = false;
	private List<PartyMember> members = new ArrayList<PartyMember>();
	private HashMap<UUID, Long> invitations = new HashMap<UUID, Long>();

	protected Party(PartyManager manager, UUID leader)
	{
		this.manager = manager;
		addMember(leader, PartyRank.OWNER);
	}

	public PartyMember getLeader()
	{
		for (PartyMember pm : members)
			if (pm.getRank().equals(PartyRank.OWNER))
				return pm;
		return null;
	}

	public void addMember(UUID id, PartyRank rank)
	{
		if (hasMember(id)) {
			return;
		}
		removeInvitation(id);
		this.getMembers()
				.add(new PartyMember(id, rank, System.currentTimeMillis()));
			MessageSender.broadcastParty(this, Bukkit.getPlayer(id).getName() + " has joined the party.");
		
	}

	public boolean hasMember(UUID member)
	{

		for (PartyMember pm : this.getMembers())
			if (pm.getPlayerID().equals(member))
				return true;
		return false;
	}
	
	public PartyMember getMember(UUID member) {
		for (PartyMember pm : this.getMembers())
			if (pm.getPlayerID().equals(member))
				return pm;
		return null;
	}
	
	public PartyRank getRank(UUID member) {
		PartyMember pm = getMember(member);
		if (pm == null) {
			return null;
		}
		return pm.getRank();
	}
	
	public boolean isRankOrGreater(UUID member, PartyRank rank) {
		PartyRank currRank = getRank(member);
		if (currRank == null) {
			return false;
		}
		return currRank.ordinal() >= rank.ordinal();
	}
	
	public void removeMember(UUID member, boolean kick) {
		if (!hasMember(member)) {
			return;
		}
		PartyMember pm = getMember(member);
		MessageSender.broadcastParty(this, Bukkit.getPlayer(member).getName() + " has " + (kick ? "been kicked from" : "left") +" the party.");
		members.remove(pm);
	}
	
	public void disband() {
		MessageSender.broadcastParty(this, "The party has been disbanded.");
		manager.removeParty(this);
	}
	
	public boolean isInvited(UUID id) {
		return invitations.containsKey(id);
	}
	
	public void removeInvitation(UUID id) {
		invitations.remove(id);
	}
	
	protected void checkExpiredInvitations() {
		List<UUID> expired = new ArrayList<UUID>();
		for (Entry<UUID, Long> e : invitations.entrySet()) {
			if (e.getValue() + (60 * 1000) > System.currentTimeMillis()) {
				MessageSender.sendInfoMessage(e.getKey(), "Your invitation to " + Bukkit.getOfflinePlayer(getLeader().getPlayerID()).getName() +
				"'s party has been expired");
				expired.add(e.getKey());
			}
		}
		expired.forEach(invitations::remove);
	}
	
	public void invite(UUID id) {
		if (hasMember(id)) {
			return;
		}
		invitations.put(id, System.currentTimeMillis());
		String name = Bukkit.getOfflinePlayer(getLeader().getPlayerID()).getName();
		MessageSender.sendInfoMessage(id, "You have been invited to " + name + "'s party."
				+ " to accept, type /party join " + name + " (the invitation will expire in 60 seconds)");
	}

	public boolean isOpen()
	{
		return isOpen;
	}

	public void setOpen(boolean isOpen)
	{
		this.isOpen = isOpen;
	}

	public List<PartyMember> getMembers()
	{
		return members;
	}

	@Override
	public String toString()
	{
		String s = "";
		s += ChatColor.AQUA + "______" + ChatColor.YELLOW + Bukkit
				.getOfflinePlayer(this.getLeader().getPlayerID()).getName()
				+ "'s Party" + ChatColor.AQUA + "______\n";
		s += ChatColor.YELLOW + "Is Public: "
				+ (isOpen ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No")
				+ "\n";
		for (PartyMember pm : members)
		{
			s+= ChatColor.GREEN + pm.toString() + "\n";
		}
		s+= ChatColor.AQUA + "____________________";
		return s;
	}
}
