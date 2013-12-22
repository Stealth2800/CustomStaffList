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
package com.stealthyone.mcb.customuserlist.commands;

import com.stealthyone.mcb.customuserlist.CustomUserList;
import com.stealthyone.mcb.customuserlist.backend.userlists.UserList;
import com.stealthyone.mcb.customuserlist.messages.ErrorMessage;
import com.stealthyone.mcb.customuserlist.messages.NoticeMessage;
import com.stealthyone.mcb.customuserlist.messages.UsageMessage;
import com.stealthyone.mcb.customuserlist.permissions.PermissionNode;
import com.stealthyone.mcb.stbukkitlib.lib.updating.UpdateChecker;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public final class CmdUserList implements CommandExecutor {

    private CustomUserList plugin;

    public CmdUserList(CustomUserList plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("customuserlist")) {
            if (args.length > 0) {
                switch (args[0]) {
                    /* List UserLists */
                    case "list":
                        cmdList(sender, cmd, label, args);
                        return true;

                    /* View UserList */
                    case "show":
                        cmdShow(sender, cmd, label, args);
                        return true;

                    /* Reload command */
                    case "reload":
                        cmdReload(sender, cmd, label, args);
                        return true;

                    /* Version command */
                    case "version":
                        cmdVersion(sender, cmd, label, args);
                        break;
                }
            }
            cmdVersion(sender, cmd, label, args);
        } else {
            UserList userList = plugin.getUserListBackend().getUserList(label);
            if (userList == null) {
                sender.sendMessage(ChatColor.RED + "ERROR - you shouldn't be seeing this...");
            } else if (!PermissionNode.checkCustomPermission(userList.getPermission(), sender)) {
                ErrorMessage.NO_PERMISSION.sendTo(sender);
            } else {
                for (String message : userList.constructList()) sender.sendMessage(message);
            }
        }
        return true;
    }

    /*
     * List all UserLists
     */
    private void cmdList(CommandSender sender, Command command, String label, String[] args) {
        if (!PermissionNode.LIST.isAllowed(sender)) {
            ErrorMessage.NO_PERMISSION.sendTo(sender);
        } else {
            List<String> messages = new ArrayList<>();
            for (Entry<Integer, UserList> entry : CustomUserList.getInstance().getUserListBackend().getAllUserLists().entrySet()) {
                messages.add(ChatColor.YELLOW + entry.getValue().getName());
            }
            sender.sendMessage(ChatColor.DARK_GRAY + "=====" + ChatColor.GREEN + "User Lists" + ChatColor.DARK_GRAY + "=====");
            sender.sendMessage(messages.toString().replace("[", "").replace("]", "").replace(",", ChatColor.DARK_GRAY + ","));
        }
    }

    /*
     * View UserList
     */
    private void cmdShow(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            UsageMessage.STAFFLIST_SHOW.sendTo(sender, label);
        } else {
            UserList list = plugin.getUserListBackend().getUserList(args[1]);
            if (list == null) {
                ErrorMessage.INVALID_USERLIST.sendTo(sender, args[1]);
            } else if (!PermissionNode.checkCustomPermission(list.getPermission(), sender)) {
                ErrorMessage.NO_PERMISSION.sendTo(sender);
            } else {
                for (String message : list.constructList()) sender.sendMessage(message);
            }
        }
    }

    /*
     * Reload plugin
     */
    private void cmdReload(CommandSender sender, Command command, String label, String[] args) {
        if (!PermissionNode.ADMIN_RELOAD.isAllowed(sender)) {
            ErrorMessage.NO_PERMISSION.sendTo(sender);
        } else {
            plugin.reloadAll();
            NoticeMessage.PLUGIN_RELOADED.sendTo(sender);
        }
    }

    /*
     * Plugin version
     */
    private void cmdVersion(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.GREEN + plugin.getName() + ChatColor.GOLD + " v" + plugin.getVersion());
        sender.sendMessage(ChatColor.GOLD + "Created by Stealth2800");
        sender.sendMessage(ChatColor.GOLD + "Website: " + ChatColor.AQUA + "http://stealthyone.com/bukkit");
        UpdateChecker updateChecker = plugin.getUpdateChecker();
        if (updateChecker.checkForUpdates()) {
            String curVer = plugin.getVersion();
            String remVer = updateChecker.getNewVersion().replace("v", "");
            sender.sendMessage(ChatColor.RED + "A different version was found on BukkitDev! (Current: " + curVer + " | Remote: " + remVer + ")");
            sender.sendMessage(ChatColor.RED + "You can download it from " + updateChecker.getVersionLink());
        }
    }

}