package com.omerg.party;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;

import net.md_5.bungee.api.ChatColor;

public class Party
{
	private boolean isOpen = false;
	private List<PartyMember> members = new ArrayList<PartyMember>();

	protected Party(UUID leader)
	{
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
		this.getMembers()
				.add(new PartyMember(id, rank, System.currentTimeMillis()));
		for (PartyMember pm : members)
		{
			MessageSender.sendInfoMessage(pm.getPlayerID(),
					Bukkit.getPlayer(id).getName() + " has joined the party.");
		}
	}

	public boolean hasMember(UUID member)
	{

		for (PartyMember pm : this.getMembers())
			if (pm.getPlayerID().equals(member))
				return true;
		return false;
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
