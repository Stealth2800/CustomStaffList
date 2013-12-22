/*
 *               The Building Game - Bukkit Plugin
 * Copyright (C) 2013 Stealth2800 <stealth2800@stealthyone.com>
 *               Website: <http://stealthyone.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.stealthyone.mcb.customuserlist.listeners;

import com.stealthyone.mcb.customuserlist.CustomStaffList;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Map.Entry;

public class PlayerListener implements Listener {

    private CustomStaffList plugin;

    public PlayerListener(CustomStaffList plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        for (Entry<String, Integer> entry : plugin.getUserListBackend().getRegisteredAliases().entrySet()) {
            if (e.getMessage().startsWith("/" + entry.getKey())) {
                plugin.getCommand("customuserlist").execute(e.getPlayer(), entry.getKey(), new String[]{});
                e.setCancelled(true);
            }
        }
    }

}