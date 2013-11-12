/*
 *               CustomStaffList - Bukkit Plugin
 * Copyright (C) 2013 Stealth2800 <stealth2800@stealthyone.com>
 *              Website: <http://stealthyone.com/>
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
package com.stealthyone.mcb.customstafflist.backend.userlists;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

public class ListGroup {

    private ConfigurationSection config;

    public ListGroup(ConfigurationSection config) {
        this.config = config;
    }

    public String getDisplayName() {
        return config.getString("display name", config.getName());
    }

    public ChatColor getColor() {
        try {
            return ChatColor.valueOf(config.getString("color"));
        } catch (Exception ex) {
            return ChatColor.WHITE;
        }
    }

    public ChatColor getNameColor() {
        try {
            return ChatColor.valueOf(config.getString("name color"));
        } catch (Exception ex) {
            return getColor();
        }
    }

    public String getPermission() {
        return config.getString("permission", "");
    }

    public boolean ignoreOp() {
        return config.getBoolean("ignore op", true);
    }

}