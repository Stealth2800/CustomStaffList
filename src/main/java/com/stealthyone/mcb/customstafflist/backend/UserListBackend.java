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
package com.stealthyone.mcb.customstafflist.backend;

import com.stealthyone.mcb.customstafflist.CustomStaffList;
import com.stealthyone.mcb.customstafflist.CustomStaffList.Log;
import com.stealthyone.mcb.customstafflist.backend.userlists.UserList;
import com.stealthyone.mcb.stbukkitlib.lib.storage.YamlFileManager;
import com.stealthyone.mcb.stbukkitlib.lib.utils.FileUtils;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserListBackend {

    private CustomStaffList plugin;

    private YamlFileManager userlistFile;
    private Map<Integer, UserList> loadedUserLists = new HashMap<Integer, UserList>();
    private Map<String, Integer> registeredAliases = new HashMap<String, Integer>();

    public UserListBackend(CustomStaffList plugin) {
        this.plugin = plugin;
        userlistFile = new YamlFileManager(plugin.getDataFolder() + File.separator + "userlists.yml");
        if (userlistFile.isEmpty()) {
            Log.info("Creating default userlists.yml file");
            FileUtils.copyGenericFileFromJar(plugin, "userlists.yml");
            userlistFile.reloadConfig();
        }
        Log.info("Loaded " + reloadUserLists() + " user lists.");
    }

    private int getNextId() {
        return loadedUserLists.size() + 1;
    }

    public int reloadUserLists() {
        registeredAliases.clear();
        loadedUserLists.clear();
        FileConfiguration userlistConfig = userlistFile.getConfig();
        PluginCommand command = plugin.getCommand("customstafflist");
        command.getAliases().clear();
        for (String listName : userlistConfig.getKeys(false)) {
            UserList list = new UserList(userlistConfig.getConfigurationSection(listName));
            int id = getNextId();
            List<String> aliases = list.getAliases();
            if (aliases == null) {
                Log.severe("Error loading UserList '" + listName + "' - invalid aliases");
                continue;
            }
            loadedUserLists.put(id, list);
            addAliases(id, aliases.toArray(new String[aliases.size()]));
        }
        return loadedUserLists.size();
    }

    public void addAliases(int listId, String... aliases) {
        String listName = loadedUserLists.get(listId).getName();
        for (String alias : aliases) {
            alias = alias.toLowerCase();
            if (registeredAliases.get(alias) != null) {
                Log.warning("Error registering alias for list '" + listName + "' - alias already registered for list '" + getUserList(alias) + "'");
                continue;
            }
            registeredAliases.put(alias, listId);
        }
        Log.info("Registered " + aliases.length + " aliases for list '" + listName + "'");
    }

    public UserList getUserList(String alias) {
        Integer listId = registeredAliases.get(alias.toLowerCase());
        if (listId == null) return null;
        return loadedUserLists.get(listId);
    }

    public Map<String, Integer> getRegisteredAliases() {
        return registeredAliases;
    }

}