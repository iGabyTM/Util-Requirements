package me.gabytm.util.requirements;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

public interface Requirement {

    String getType();

    default List<String> getAliases() {
        return null;
    }

    boolean eval(final Player player, final ConfigurationSection section, final RequirementsManager.DenyCommandsExecution denyCommandsExecution);
}
