package com.omerg.party;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args)
	{
		//Join
		//Make Open
		//Kick
		//Invite Player
		//Change Rank
		//Disband
		//Leave
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
					if (Party.getPartyByMember(p.getUniqueId()) != null)
					{
						p.sendMessage(Party.getPartyByMember(p.getUniqueId())
								.toString());
						return true;
					} else
					{
						MessageSender.sendErrorMessage(p.getUniqueId(),
								"You're not part of a party.");
						return true;
					}
				}
			}
			if (args.length == 2)
			{
				Player p = (Player) sender;
				if (Bukkit.getPlayer(args[1]) != null)
				{
					if (Party.getPartyByMember(
							Bukkit.getPlayer(args[1]).getUniqueId()) != null)
					{
						p.sendMessage(Party
								.getPartyByMember(
										Bukkit.getPlayer(args[1]).getUniqueId())
								.toString());
						return true;
					} else
					{
						MessageSender.sendErrorMessage(p.getUniqueId(),
								"Couldn't find this player's party.");
						return true;
					}
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
