/*
 * Bukkit plugin: CustomStaffList
 * Copyright (C) 2013 Stealth2800 <stealth2800@stealthyone.com>
 * Website: <http://stealthyone.com/bukkit>
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
package com.stealthyone.mcb.customuserlist.backend.userlists;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

public class UserListFormat {

    public final String DEFAULT_HEADER = "";
    public final String DEFAULT_NAME = "&8----&6Online Users&8----";
    public final String DEFAULT_GROUP_FORMAT = "&6[{GROUPNAME}&6]&f";
    public final String DEFAULT_FOOTER = "";
    public final String DEFAULT_NONE_ONLINE = "&c&oNone online.";
    public final boolean DEFAULT_USE_DISPLAY_NAMES = false;
    public final boolean DEFAULT_LIMIT_TO_ONE_GROUP = true;

    private ConfigurationSection config;

    public UserListFormat(ConfigurationSection config) {
        this.config = config;
    }

    public String getHeader() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("Format.header", DEFAULT_HEADER));
    }

    public String getName() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("Format.name", DEFAULT_NAME));
    }

    public String getGroupNameFormat() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("Format.group", DEFAULT_GROUP_FORMAT));
    }

    public String getFooter() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("Format.footer", DEFAULT_FOOTER));
    }

    public String getNoneOnlineMessage() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("Format.none online", DEFAULT_NONE_ONLINE));
    }

    public boolean useDisplayNames() {
        return config.getBoolean("Format.display names", DEFAULT_USE_DISPLAY_NAMES);
    }

    public boolean limitPlayersToOneGroup() {
        return config.getBoolean("Format.limit to one group", DEFAULT_LIMIT_TO_ONE_GROUP);
    }

    public boolean hideVanished() {
        return config.getBoolean("hide vanished", true);
    }

}