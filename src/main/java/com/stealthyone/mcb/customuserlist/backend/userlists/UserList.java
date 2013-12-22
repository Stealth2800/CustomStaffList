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
package com.stealthyone.mcb.customuserlist.backend.userlists;

import com.stealthyone.mcb.customuserlist.CustomStaffList.Log;
import com.stealthyone.mcb.customuserlist.backend.hooks.VanishHelper;
import com.stealthyone.mcb.customuserlist.permissions.PermissionNode;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserList {

    private ConfigurationSection config;

    private UserListFormat format;
    private List<ListGroup> groups = new ArrayList<ListGroup>();

    public UserList(ConfigurationSection config) {
        this.config = config;
        format = new UserListFormat(config);
        Log.info("Loaded " + reloadGroups() + " groups for UserList type: " + config.getName());
    }

    public String getName() {
        return config.getName();
    }

    public int reloadGroups() {
        groups.clear();
        ConfigurationSection groupSection = config.getConfigurationSection("Groups");
        for (String groupName : groupSection.getKeys(false)) {
            ListGroup group = new ListGroup(groupSection.getConfigurationSection(groupName));
            groups.add(group);
        }
        return groups.size();
    }

    public List<String> getAliases() {
        Object aliases = config.get("aliases");
        if (aliases instanceof String) {
            return Arrays.asList((String) aliases);
        } else if (aliases instanceof List) {
            return (List<String>) aliases;
        } else {
            return null;
        }
    }

    public String getPermission() {
        return config.getString("permission");
    }

    public List<String> constructList() {
        List<String> returnList = new ArrayList<String>();
        Player[] onlinePlayers = Bukkit.getOnlinePlayers();
        boolean useDisplayNames = format.useDisplayNames();
        List<String> addedNames = new ArrayList<String>();

        /* Add header */
        String header = format.getHeader();
        if (header.length() > 0) returnList.add(header);

        /* Add name */
        returnList.add(format.getName());

        /* Construct groups */
        for (ListGroup group : groups) {
            StringBuilder groupLine = new StringBuilder();
            groupLine.append(format.getGroupNameFormat().replace("{GROUPNAME}", group.getColor() + group.getDisplayName()));

            /* Check players */
            StringBuilder playerList = new StringBuilder();
            String permission = group.getPermission();
            for (Player player : onlinePlayers) {
                if (PermissionNode.checkCustomPermission(permission, player, group.ignoreOp())) {
                    if (VanishHelper.isPlayerVanished(player)) continue;
                    String playerName = useDisplayNames ? player.getDisplayName() : player.getName();
                    if (format.limitPlayersToOneGroup() && addedNames.contains(playerName)) continue;
                    addedNames.add(playerName);
                    playerList.append(group.getNameColor() + (playerList.length() > 0 ? ", " + playerName : playerName));
                }
            }

            groupLine.append(playerList.length() == 0 ? format.getNoneOnlineMessage() : playerList.toString());
            returnList.add(groupLine.toString());
        }

        /* Add footer */
        String footer = format.getFooter();
        if (footer.length() > 0) returnList.add(footer);

        return returnList;
    }

}