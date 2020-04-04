package me.gabytm.util.requirements.requirements.permission;

import me.gabytm.util.requirements.DefaultRequirements;
import me.gabytm.util.requirements.Requirement;
import me.gabytm.util.requirements.RequirementsManager;
import me.gabytm.util.requirements.utils.StringUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class HasPermission implements Requirement {
    private final RequirementsManager manager;
    private final boolean inverted;

    public HasPermission(RequirementsManager manager, boolean inverted) {
        this.manager = manager;
        this.inverted = inverted;
    }

    @Override
    public String getType() {
        return inverted ? DefaultRequirements.HAS_PERMISSION_REVERSED.getType() : DefaultRequirements.HAS_PERMISSION.getType();
    }

    @Override
    public boolean eval(Player player, ConfigurationSection section, RequirementsManager.DenyCommandsExecution denyCommandsExecution) {
        final String permission = StringUtil.replacePlaceholders(player, section.getString("permission", ""));
        final boolean result = player.hasPermission(permission);

        if (result == inverted && denyCommandsExecution == RequirementsManager.DenyCommandsExecution.EACH) {
            manager.executeDenyCommands(player, section);
        }

        return result != inverted;
    }
}
