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
package com.stealthyone.mcb.customuserlist.config;

import com.stealthyone.mcb.customuserlist.CustomUserList;

public enum ConfigBoolean {

    CHECK_FOR_UPDATES("Check for updates"),
    DEBUG("Debug"),
    HIDE_VANISHED("Hide vanished players"),
    SHOW_EMPTY_GROUPS("Format.Show groups with no one on"),
    USE_PLAYER_DISP_NAME("Format.Use player display name");

    private String path;
    private boolean defaultValue;

    private ConfigBoolean(String path) {
        this.path = path;
        this.defaultValue = false;
    }

    public boolean get() {
        return CustomUserList.getInstance().getConfig().getBoolean(path);
    }

}