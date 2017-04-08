package com.omerg.party;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor
{
	
	private AfterParty plugin;
	
	public CommandManager(AfterParty plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args)
	{
		if (cmd.getName().equalsIgnoreCase("party"))
		{
			if (!(sender instanceof Player))
			{
				sender.sendMessage(
						"Sorry Mate, This plugin is made for the players!");
				return false;
			}
			if (args.length == 0)
			{
				displayHelpMessage(sender);
				return true;
			}
			if (args.length == 1)
			{
				if (args[0].equalsIgnoreCase("help"))
				{
					displayHelpMessage(sender);
					return true;
				}
				if (args[0].equalsIgnoreCase("check"))
				{
					Player p = (Player) sender;
					if (plugin.getPartyManager().getPartyByMember(p.getUniqueId()) != null)
					{
						p.sendMessage(plugin.getPartyManager().getPartyByMember(p.getUniqueId())
								.toString());
						return true;
					} else
					{
						MessageSender.sendErrorMessage(p.getUniqueId(),
								"You're not part of a party.");
						return true;
					}
				} else if (args[0].equalsIgnoreCase("leave")) {
					Player p = (Player) sender;
					Party party = plugin.getPartyManager().getPartyByMember(p.getUniqueId());
					if (party == null) {
						MessageSender.sendErrorMessage(p.getUniqueId(), "You're not part of a party.");
						return true;
					}
					party.removeMember(p.getUniqueId(), false);
				} else if (args[0].equalsIgnoreCase("disband")) {
					Player p = (Player) sender;
					Party party = plugin.getPartyManager().getPartyByMember(p.getUniqueId());
					if (party == null) {
						MessageSender.sendErrorMessage(p.getUniqueId(), "You're not part of a party.");
						return true;
					}
					if (party.isRankOrGreater(p.getUniqueId(), PartyRank.OWNER)) {
						party.disband();
					} else {
						MessageSender.sendErrorMessage(p.getUniqueId(), "You're not the party leader");
					}
				}
			}
			if (args.length == 2)
			{
				if (args[0].equalsIgnoreCase("invite")) {
					Player p = (Player) sender;
					Party party = plugin.getPartyManager().getPartyByMember(p.getUniqueId());
					if (party == null) {
						MessageSender.sendErrorMessage(p.getUniqueId(), "You're not part of a party.");
						return true;
					}
					if (party.isRankOrGreater(p.getUniqueId(), PartyRank.COOWNER)) {
						Player invite = Bukkit.getPlayer(args[1]);
						if (invite == null) {
							MessageSender.sendErrorMessage(p.getUniqueId(), "Player '" + args[1] + "' not found");
							return true;
						}
						
						if (party.isInvited(invite.getUniqueId())) {
							MessageSender.sendErrorMessage(p.getUniqueId(), invite.getName() + " already invited to the party");
							return true;
						}
						
						if (party.hasMember(invite.getUniqueId())) {
							MessageSender.sendErrorMessage(p.getUniqueId(), invite.getName() + " already in the party.");
							return true;
						}
						
						party.invite(invite.getUniqueId());
						MessageSender.sendInfoMessage(p.getUniqueId(), "You have invited " + invite.getName() + " to thr party");
					} else {
						MessageSender.sendErrorMessage(p.getUniqueId(), "You can't invite players to the party");
					}
				} else if (args[0].equalsIgnoreCase("join")) {
					Player p = (Player) sender;
					Party party = plugin.getPartyManager().getPartyByLeaderName(args[1]);
					if (party == null) {
						MessageSender.sendErrorMessage(p.getUniqueId(), "Party not found");
						return true;
					}
					
					if (party.hasMember(p.getUniqueId())) {
						MessageSender.sendErrorMessage(p.getUniqueId(), "You are already in that party");
						return true;
					}
					
					if (!party.isOpen() && !party.isInvited(p.getUniqueId())) {
						MessageSender.sendErrorMessage(p.getUniqueId(), "You aren't invited to that party");
						return true;
					}
					party.addMember(p.getUniqueId(), PartyRank.MEMBER);
				} else if (args[0].equalsIgnoreCase("open")) {
					Player p = (Player) sender;
					Party party = plugin.getPartyManager().getPartyByMember(p.getUniqueId());
					if (party == null) {
						MessageSender.sendErrorMessage(p.getUniqueId(), "You're not part of a party.");
						return true;
					}
					if (party.isRankOrGreater(p.getUniqueId(), PartyRank.COOWNER)) {
						boolean open = true;
						if (args[1].equalsIgnoreCase("open") || args[1].equalsIgnoreCase("true")) {
							open = true;
						} else if (args[1].equalsIgnoreCase("close") || args[1].equalsIgnoreCase("false")) {
							open = false;
						} else {
							MessageSender.sendErrorMessage(p.getUniqueId(), "Invalid option");
							return true;
						}
						
						party.setOpen(open);
						MessageSender.sendInfoMessage(p.getUniqueId(), "The party is now " + (open ? "open" : "closed"));
					}
				} else if (args[0].equalsIgnoreCase("kick")) {
					Player p = (Player) sender;
					Party party = plugin.getPartyManager().getPartyByMember(p.getUniqueId());
					if (party == null) {
						MessageSender.sendErrorMessage(p.getUniqueId(), "You're not part of a party.");
						return true;
					}
					
					if (party.isRankOrGreater(p.getUniqueId(), PartyRank.COOWNER)) {
						Player kick = Bukkit.getPlayer(args[1]);
						if (kick == null) {
							MessageSender.sendErrorMessage(p.getUniqueId(), "Player '" + args[1] + "' not found");
							return true;
						}
						if (!party.hasMember(kick.getUniqueId())) {
							MessageSender.sendErrorMessage(p.getUniqueId(), "The player " + kick.getName() + " is not in the party");
							return true;
						}
						
						party.removeMember(kick.getUniqueId(), true);
					} else {
						MessageSender.sendErrorMessage(p.getUniqueId(), "You can't kick players out of the party");
					}
				} else if (args[0].equalsIgnoreCase("check")) {
					Player p = (Player) sender;
					Player target = Bukkit.getPlayer(args[1]);
					if (target != null) {
						Party party = plugin.getPartyManager().getPartyByMember(target.getUniqueId());
						if (party != null) {
							p.sendMessage(party.toString());
							return true;
						} else {
							MessageSender.sendErrorMessage(p.getUniqueId(), "Couldn't find this player's party.");
							return true;
						}
					} else {
						MessageSender.sendErrorMessage(p.getUniqueId(), "Player not found");
					}
				}
			} else if (args.length == 3) {
				if (args[0].equalsIgnoreCase("changerank")) {
					Player p = (Player) sender;
					Party party = plugin.getPartyManager().getPartyByMember(p.getUniqueId());
					if (party == null) {
						MessageSender.sendErrorMessage(p.getUniqueId(), "You're not part of a party.");
						return true;
					}
					
					Player target = Bukkit.getPlayer(args[1]);
					if (target == null) {
						MessageSender.sendErrorMessage(p.getUniqueId(), "Player not found");
						return true;
					}
					if (!party.hasMember(target.getUniqueId())) {
						MessageSender.sendErrorMessage(p.getUniqueId(), target.getName() + " is not in the party");
						return true;
					}
					
					PartyRank playerrank = party.getRank(p.getUniqueId());
					if (party.getRank(target.getUniqueId()).ordinal() >= playerrank.ordinal() && playerrank != PartyRank.OWNER) {
						MessageSender.sendErrorMessage(p.getUniqueId(), "You can't change the rank of " + target.getName());
						return true;
					}
					
					PartyRank rank = null;
					try {
						rank = PartyRank.valueOf(args[2].toUpperCase());
					} catch (IllegalArgumentException e) {
						MessageSender.sendErrorMessage(p.getUniqueId(), "Invalid rank");
						return true;
					}
					
					boolean canSet = rank.ordinal() < playerrank.ordinal();
					if (!canSet) {
						MessageSender.sendErrorMessage(p.getUniqueId(), "You can't change players rank to " + rank.name().toLowerCase());
						return true;
					}
					if (rank == PartyRank.OWNER) {
						party.getMember(p.getUniqueId()).setRank(PartyRank.COOWNER);
						party.getMember(target.getUniqueId()).setRank(PartyRank.OWNER);
						MessageSender.broadcastParty(party, target.getName() + " is now the party leader");
					} else {
						party.getMember(target.getUniqueId()).setRank(rank);
					}
					MessageSender.sendInfoMessage(p.getUniqueId(), "You've changed " + target.getName() + " rank to " + rank.name().toLowerCase());
					
				}
			}
		}
		return false;
	}

	private void displayHelpMessage(CommandSender sender)
	{
		// HELP MESSAGE;

	}

}
