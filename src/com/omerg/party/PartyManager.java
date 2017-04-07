package com.omerg.party;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PartyManager {

	private List<Party> parties = new ArrayList<Party>();

	public Party createParty(UUID leader) {
		if (getPartyByMember(leader) == null) {
			Party party = new Party(leader);
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

	public Party getPartyByMember(UUID member) {
		for (Party par : parties)
			if (par.hasMember(member))
				return par;
		return null;
	}

}
