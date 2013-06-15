package com.stealthyone.bukkit.customstafflist.hooks;

import org.kitteh.vanish.staticaccess.VanishNoPacket;
import org.kitteh.vanish.staticaccess.VanishNotLoadedException;

import com.stealthyone.bukkit.customstafflist.CustomStaffList;

public final class HookVanishNoPacket {

	private CustomStaffList plugin;
	
	public HookVanishNoPacket(CustomStaffList plugin) {
		this.plugin = plugin;
	}
	
	public boolean isVanished(String name) {
		try {
			return VanishNoPacket.isVanished(name);
		} catch (VanishNotLoadedException e) {
			return false;
		}
	}
}