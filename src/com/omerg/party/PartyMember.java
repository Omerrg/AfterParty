package com.omerg.party;

import java.util.UUID;

import org.bukkit.Bukkit;

public class PartyMember
{
	private UUID playerID;
	private PartyRank rank;
	private long whenJoined;

	public PartyMember(UUID id)
	{
		this.playerID = id;
		this.rank = PartyRank.MEMBER;
		this.whenJoined = System.currentTimeMillis();
	}

	public PartyMember(UUID id, PartyRank rank)
	{
		this.playerID = id;
		this.rank = rank;
		this.whenJoined = System.currentTimeMillis();
	}

	public PartyMember(UUID id, PartyRank rank, long whenJoined)
	{
		this.playerID = id;
		this.rank = rank;
		this.whenJoined = whenJoined;
	}

	public PartyRank getRank()
	{
		return rank;
	}

	public void setRank(PartyRank rank)
	{
		this.rank = rank;
	}

	public UUID getPlayerID()
	{
		return playerID;
	}

	public long getWhenJoined()
	{
		return whenJoined;
	}
	@Override
	public String toString()
	{
		long milliseconds = System.currentTimeMillis() - getWhenJoined();
		
		int seconds = (int) (milliseconds / 1000) % 60 ;
		int minutes = (int) ((milliseconds / (1000*60)) % 60);
		int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
		int days = (int) (milliseconds / (1000*60*60*24));
		
		return Bukkit.getOfflinePlayer(playerID).getName() + " : " + getRank().name() + ", Joined Before: " + days + "d " + hours + "h " + minutes + "m " + seconds + "s"; 
	}

}
