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
package com.stealthyone.mcb.customstafflist;

import com.stealthyone.mcb.customstafflist.backend.UserListBackend;
import com.stealthyone.mcb.customstafflist.commands.CmdStaffList;
import com.stealthyone.mcb.customstafflist.config.ConfigHelper;
import com.stealthyone.mcb.customstafflist.listeners.PlayerListener;
import com.stealthyone.mcb.stbukkitlib.lib.hooks.HookHelper;
import com.stealthyone.mcb.stbukkitlib.lib.messages.MessageRetriever;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomStaffList extends JavaPlugin {

    public final static class Log {

        public static void debug(String msg) {
            if (ConfigHelper.DEBUG.getBoolean())
                instance.logger.log(Level.INFO, String.format("[%s DEBUG] %s", CustomStaffList.getInstance().getName(), msg));
        }

        public static void info(String msg) {
            instance.logger.log(Level.INFO, String.format("[%s] %s", CustomStaffList.getInstance().getName(), msg));
        }

        public static void warning(String msg) {
            instance.logger.log(Level.WARNING, String.format("[%s] %s", CustomStaffList.getInstance().getName(), msg));
        }

        public static void severe(String msg) {
            instance.logger.log(Level.SEVERE, String.format("[%s] %s", CustomStaffList.getInstance().getName(), msg));
        }
    }

    private static CustomStaffList instance;
    {
        instance = this;
    }

    public final static CustomStaffList getInstance() {
        return instance;
    }

    private Logger logger;

    private boolean vanishHook;

    private MessageRetriever messageManager;

    private UserListBackend userListBackend;

    @Override
    public void onLoad() {
        logger = getServer().getLogger();
        getDataFolder().mkdir();
    }

    @Override
    public void onEnable() {
        /* Setup config */
        saveDefaultConfig();
        getConfig().options().copyDefaults(false);
        saveConfig();

        /* Setup hooks */
        vanishHook = HookHelper.hookVanishNoPacket();
        if (vanishHook) {
            String vanishVersion = getServer().getPluginManager().getPlugin("VanishNoPacket").getDescription().getVersion();
            Log.info(String.format("Successfully hooked with VanishNoPacket v%s", vanishVersion));
        } else {
            Log.info("Didn't find VanishNoPacket.");
        }

        /* Check config value for hiding vanished players */
        if (ConfigHelper.HIDE_VANISHED.getBoolean()) {
            if (!vanishHook)
                Log.info("Config has 'Hide vanished players' set to TRUE but VanishNoPacket is not installed!");
            else
                Log.info("Hiding vanished players ENABLED.");
        } else {
            Log.info("Hiding vanished players DISABLED.");
        }

        /* Setup important plugin components */
        messageManager = new MessageRetriever(this);

        userListBackend = new UserListBackend(this);

        /* Register listeners */
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);

        /* Register commands */
        getCommand("customstafflist").setExecutor(new CmdStaffList(this));

        Log.info(String.format("%s v%s by Stealth2800 enabled.", getName(), getVersion()));
    }

    @Override
    public void onDisable() {
        Log.info(String.format("%s v%s by Stealth2800 disabled.", getName(), getVersion()));
    }

    public void reloadAll() {
        reloadConfig();
        getUserListBackend().reloadUserLists();
    }

    public String getVersion() {
        return this.getDescription().getVersion();
    }

    public MessageRetriever getMessageManager() {
        return messageManager;
    }

    public UserListBackend getUserListBackend() {
        return userListBackend;
    }

}