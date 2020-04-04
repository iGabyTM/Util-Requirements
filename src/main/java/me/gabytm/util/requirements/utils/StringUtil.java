package me.gabytm.util.requirements.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class StringUtil {
    public static String replacePlaceholders(final Player player, final String string) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return PlaceholderAPI.setPlaceholders(player, string);
        }

        return StringUtils.replace(string, "%player_name%", player.getName());
    }
}
