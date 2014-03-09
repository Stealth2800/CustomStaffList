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
package com.stealthyone.mcb.customuserlist.backend;

import com.stealthyone.mcb.customuserlist.CustomUserList;
import com.stealthyone.mcb.customuserlist.CustomUserList.Log;
import com.stealthyone.mcb.customuserlist.backend.userlists.UserList;
import com.stealthyone.mcb.customuserlist.config.ConfigHelper;
import com.stealthyone.mcb.customuserlist.utils.YamlFileManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.SimplePluginManager;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserListBackend {

    private CustomUserList plugin;

    private YamlFileManager userlistFile;
    private Map<Integer, UserList> loadedUserLists = new HashMap<Integer, UserList>();
    private Map<String, Integer> registeredAliases = new HashMap<String, Integer>();

    public UserListBackend(CustomUserList plugin) {
        this.plugin = plugin;
        userlistFile = new YamlFileManager(plugin.getDataFolder() + File.separator + "userlists.yml");
        if (userlistFile.isEmpty()) {
            Log.info("Creating default userlists.yml file");
            plugin.saveResource("userlists.yml", true);
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
        userlistFile.reloadConfig();
        FileConfiguration userlistConfig = userlistFile.getConfig();
        PluginCommand command = plugin.getCommand("customuserlist");
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
        Command mainCommand = plugin.getCommand("customuserlist");
        SimpleCommandMap commandMap;
        Method methodRegisterCommand;
        try {
            SimplePluginManager pluginManager = (SimplePluginManager) Bukkit.getPluginManager();
            Class classSimplePluginManager = SimplePluginManager.class;
            Field fieldCommandMap = classSimplePluginManager.getDeclaredField("commandMap");
            fieldCommandMap.setAccessible(true);

            Log.debug("CommandMap class: " + fieldCommandMap.get(pluginManager).getClass().getCanonicalName());
            commandMap = (SimpleCommandMap) fieldCommandMap.get(pluginManager);

            Class<SimpleCommandMap> classSimpleCommandMap = SimpleCommandMap.class;

            StringBuilder declaredMethods = new StringBuilder();
            for (Method method : classSimpleCommandMap.getDeclaredMethods()) {
                StringBuilder curMethod = new StringBuilder(method.getName() + " (");
                for (Type type : method.getGenericParameterTypes()) {
                    curMethod.append(type.toString() + " ");
                }
                curMethod.append(")");
                declaredMethods.append(curMethod.toString() + ", ");
            }
            Log.debug(declaredMethods.toString());

            methodRegisterCommand = classSimpleCommandMap.getDeclaredMethod("register", String.class, String.class, Command.class, boolean.class);
            methodRegisterCommand.setAccessible(true);
        } catch (Exception ex) {
            commandMap = null;
            methodRegisterCommand = null;
            Log.warning("Error encountered preparing to register UserList aliases.");
            if (!ConfigHelper.DEBUG.get()) {
                Log.warning("Set 'Debug' to 'true' in config.yml to view the stacktrace.");
            } else {
                ex.printStackTrace();
            }
        }

        String listName = loadedUserLists.get(listId).getName();
        for (String alias : aliases) {
            alias = alias.toLowerCase();
            if (registeredAliases.get(alias) != null) {
                Log.warning("Error registering alias for list '" + listName + "' - alias already registered for other list '" + getUserList(alias) + "'");
                continue;
            }

            if (commandMap != null) {
                try {
                    if ((boolean) methodRegisterCommand.invoke(commandMap, alias, "userlist", mainCommand, true)) {
                        Log.debug("Successfully registered UserList alias '" + alias + "'");
                    } else {
                        Log.warning("Unable to register UserList alias '" + alias + "' -> already in use by other command.");
                    }
                } catch (Exception ex) {
                    Log.warning("Error encountered registering UserList alias.");
                    if (!ConfigHelper.DEBUG.get()) {
                        Log.warning("Set 'Debug' to 'true' in config.yml to view the stacktrace.");
                    } else {
                        ex.printStackTrace();
                    }
                }
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

    public Map<Integer, UserList> getAllUserLists() {
        return loadedUserLists;
    }

    public Map<String, Integer> getRegisteredAliases() {
        return registeredAliases;
    }

}