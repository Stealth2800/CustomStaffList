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
package com.stealthyone.mcb.customstafflist.permissions;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public enum PermissionNode {

    ADMIN_RELOAD;

    public final static String PREFIX = "customstafflist.";

    private String permission;

    private PermissionNode() {
        permission = PREFIX + this.toString().toLowerCase().replace("_", ".");
    }

    public final String getPermission() {
        return this.permission;
    }

    public final boolean isAllowed(CommandSender sender) {
        return sender.hasPermission(this.permission);
    }

    public final boolean isAllowed(CommandSender sender, boolean ignoreOp) {
        return ignoreOp ? sender.hasPermission(new Permission(permission, PermissionDefault.FALSE)) : isAllowed(sender);
    }

    public final static boolean checkCustomPermission(String customPerm, CommandSender sender) {
        if (customPerm == null || customPerm.equalsIgnoreCase("")) return true;
        return sender.hasPermission(customPerm);
    }

    public final static boolean checkCustomPermission(String customPerm, CommandSender sender, boolean ignoreOp) {
        if (customPerm == null || customPerm.equalsIgnoreCase("")) return true;
        return ignoreOp ? sender.hasPermission(new Permission(customPerm, PermissionDefault.FALSE)) : checkCustomPermission(customPerm, sender);
    }

}