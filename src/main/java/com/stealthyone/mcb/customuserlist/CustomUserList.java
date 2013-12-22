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
package com.stealthyone.mcb.customuserlist;

import com.stealthyone.mcb.customuserlist.backend.UserListBackend;
import com.stealthyone.mcb.customuserlist.commands.CmdUserList;
import com.stealthyone.mcb.customuserlist.config.ConfigHelper;
import com.stealthyone.mcb.customuserlist.listeners.PlayerListener;
import com.stealthyone.mcb.stbukkitlib.api.Stbl;
import com.stealthyone.mcb.stbukkitlib.lib.messages.MessageManager;
import com.stealthyone.mcb.stbukkitlib.lib.updating.UpdateChecker;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomUserList extends JavaPlugin {

    public final static class Log {

        public static void debug(String msg) {
            if (ConfigHelper.DEBUG.getBoolean())
                instance.logger.log(Level.INFO, String.format("[%s DEBUG] %s", CustomUserList.getInstance().getName(), msg));
        }

        public static void info(String msg) {
            instance.logger.log(Level.INFO, String.format("[%s] %s", CustomUserList.getInstance().getName(), msg));
        }

        public static void warning(String msg) {
            instance.logger.log(Level.WARNING, String.format("[%s] %s", CustomUserList.getInstance().getName(), msg));
        }

        public static void severe(String msg) {
            instance.logger.log(Level.SEVERE, String.format("[%s] %s", CustomUserList.getInstance().getName(), msg));
        }
    }

    private static CustomUserList instance;
    {
        instance = this;
    }

    public final static CustomUserList getInstance() {
        return instance;
    }

    private Logger logger;

    private boolean vanishHook = false;
    private boolean vaultHook = false;

    private MessageManager messageManager;
    private UpdateChecker updateChecker;

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
        vanishHook = Stbl.hooks.validateHook(this, "VanishNoPacket");
        if (Stbl.hooks.validateHook(this, "Vault")) {
            Permission permission = Stbl.hooks.getVault().getPermission();
            Chat chat = Stbl.hooks.getVault().getChat();
            if (permission != null && chat != null) {
                Log.info("Prefix support enabled, hooked with " + permission.getName() + " via Vault");
                vaultHook = true;
            } else {
                Log.info("Permission group prefix support disabled, unable to find Vault permission and/or chat backend");
                vaultHook = false;
            }
        }

        /* Setup important plugin components */
        messageManager = new MessageManager(this);

        userListBackend = new UserListBackend(this);

        /* Register listeners */
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);

        /* Register commands */
        getCommand("customuserlist").setExecutor(new CmdUserList(this));

        updateChecker = UpdateChecker.scheduleForMe(this, 54231);
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

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    public UserListBackend getUserListBackend() {
        return userListBackend;
    }

    public boolean hookedWithVanish() {
        return vanishHook;
    }

    public boolean hookedWithVault() {
        return vaultHook;
    }

}