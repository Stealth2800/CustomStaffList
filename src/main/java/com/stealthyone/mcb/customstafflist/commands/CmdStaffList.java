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
package com.stealthyone.mcb.customstafflist.commands;

import com.stealthyone.mcb.customstafflist.CustomStaffList;
import com.stealthyone.mcb.customstafflist.backend.userlists.UserList;
import com.stealthyone.mcb.customstafflist.messages.ErrorMessage;
import com.stealthyone.mcb.customstafflist.messages.NoticeMessage;
import com.stealthyone.mcb.customstafflist.messages.UsageMessage;
import com.stealthyone.mcb.customstafflist.permissions.PermissionNode;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class CmdStaffList implements CommandExecutor {

    private CustomStaffList plugin;

    public CmdStaffList(CustomStaffList plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("customstafflist")) {
            if (args.length > 0) {
                switch (args[0]) {
                    /* View Userlist command */
                    case "list":
                        cmdList(sender, cmd, label, args);
                        return true;

                    /* Reload command */
                    case "reload":
                        cmdReload(sender, cmd, label, args);
                        return true;

                    /* Version command */
                    case "version":
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

    /**
     * List Userlist command
     * @param sender
     * @param command
     * @param label
     * @param args
     */
    private void cmdList(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            UsageMessage.STAFFLIST_LIST.sendTo(sender, label);
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

    /**
     * Reload plugin command
     * @param sender
     * @param command
     * @param label
     * @param args
     */
    private void cmdReload(CommandSender sender, Command command, String label, String[] args) {
        if (!PermissionNode.ADMIN_RELOAD.isAllowed(sender)) {
            ErrorMessage.NO_PERMISSION.sendTo(sender);
        } else {
            plugin.reloadAll();
            NoticeMessage.PLUGIN_RELOADED.sendTo(sender);
        }
    }

    /**
     * Version command
     * @param sender
     * @param command
     * @param label
     * @param args
     */
    private void cmdVersion(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.GREEN + plugin.getName() + ChatColor.GOLD + " v" + plugin.getVersion());
        sender.sendMessage(ChatColor.GOLD + "Created by Stealth2800");
        sender.sendMessage(ChatColor.GOLD + "Website: " + ChatColor.AQUA + "http://stealthyone.com/bukkit");
        /*Updater updateChecker = plugin.getUpdater();
        if (updateChecker.getResult() == UpdateResult.UPDATE_AVAILABLE) {
            String curVer = plugin.getDescription().getVersion();
            String remVer = updateChecker.getLatestName().replace("v", "");
            sender.sendMessage(ChatColor.RED + "A different version was found on BukkitDev! (Current: " + curVer + " | Remote: " + remVer + ")");
            sender.sendMessage(ChatColor.RED + "You can download it from " + updateChecker.getLatestFileLink());
        }*/
    }

}