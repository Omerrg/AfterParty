package com.omerg.party;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageSender
{
	private static String prefix = ChatColor.WHITE + "[" + ChatColor.YELLOW
			+ "AP" + ChatColor.WHITE + "]";

	public static boolean sendErrorMessage(UUID id, String msg)
	{
		Player p = Bukkit.getPlayer(id);
		if (p == null)
			return false;
		p.sendMessage(prefix + ChatColor.RED + msg);
		return true;
	}

	public static boolean sendInfoMessage(UUID id, String msg)
	{
		Player p = Bukkit.getPlayer(id);
		if (p == null)
			return false;
		p.sendMessage(prefix + ChatColor.AQUA + msg);
		return true;
	}

	public static boolean sendSuccssesMessage(UUID id, String msg)
	{
		Player p = Bukkit.getPlayer(id);
		if (p == null)
			return false;
		p.sendMessage(prefix + ChatColor.GREEN + msg);
		return true;
	}
}
