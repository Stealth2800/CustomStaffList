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
package com.stealthyone.mcb.customstafflist.backend.hooks;

import com.stealthyone.mcb.customstafflist.config.ConfigHelper;
import com.stealthyone.mcb.stbukkitlib.lib.hooks.HookHelper;
import org.bukkit.entity.Player;
import org.kitteh.vanish.staticaccess.VanishNoPacket;

public final class VanishHelper {

    public final static boolean isPlayerVanished(String playerName) {
        if (!HookHelper.hookVanishNoPacket()) {
            return false;
        } else {
            try {
                if (ConfigHelper.HIDE_VANISHED.getBoolean()) {
                    return VanishNoPacket.isVanished(playerName);
                } else {
                    return false;
                }
            } catch (Exception ex) {
                return false;
            }
        }
    }

    public final static boolean isPlayerVanished(Player player) {
        return isPlayerVanished(player.getName());
    }

}