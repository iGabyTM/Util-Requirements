package me.gabytm.util.requirements;

import com.google.common.collect.ImmutableMap;
import me.gabytm.util.requirements.requirements.inputoutput.InputOutputRequirement;
import me.gabytm.util.requirements.requirements.permission.HasPermission;
import me.gabytm.util.requirements.requirements.string.RegexRequirement;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequirementsManager {
    private final Map<String, Requirement> requirements = new HashMap<>();
    private String denyCommandsSection = "deny_commands";
    private DenyCommandsHandler denyCommandsHandler;

    public RequirementsManager(final String denyCommandsSection, final boolean defaults) {
        if (denyCommandsSection != null && !denyCommandsSection.trim().isEmpty()) {
            this.denyCommandsSection = denyCommandsSection;
        }

        if (defaults) {
            registerDefaultRequirements();
        }
    }

    public RequirementsManager(final String denyCommandsSection) {
        this(denyCommandsSection, true);
    }

    public RequirementsManager(final boolean defaults) {
        this(null, defaults);
    }

    public RequirementsManager() {
        this(null, true);
    }

    public void registerDefaultRequirements() {
        for (DefaultRequirements type : DefaultRequirements.values()) {
            if (!type.name().startsWith("IO_")) {
                continue;
            }

            register(new InputOutputRequirement(this, type));
        }

        register(new HasPermission(this, false));
        register(new HasPermission(this, true));
        register(new RegexRequirement(this, false));
        register(new RegexRequirement(this, true));
    }

    public boolean check(final Player player, final ConfigurationSection section, final DenyCommandsExecution denyCommandsExecution) {
        final String requirementType = section.getString("type");

        if (requirementType == null) {
            return true;
        }

        final Requirement requirement = getRequirement(requirementType);

        if (requirement == null) {
            return true;
        }

        final boolean result = requirement.eval(player, section, denyCommandsExecution);

        if (!result && denyCommandsExecution == DenyCommandsExecution.FINAL) {
            executeDenyCommands(player, section);
        }

        return result;
    }

    public boolean checkMultiple(final Player player, final ConfigurationSection section, final DenyCommandsExecution denyCommandsExecution) {
        for (String key : section.getKeys(false)) {
            final ConfigurationSection requirementSection = section.getConfigurationSection(key);
            final String type = requirementSection.getString("type");

            if (type == null) {
                continue;
            }

            final Requirement requirement = getRequirement(type);

            if (requirement == null) {
                continue;
            }

            if (!requirement.eval(player, section, denyCommandsExecution)) {
                if (denyCommandsExecution == DenyCommandsExecution.FINAL) {
                    executeDenyCommands(player, section);
                }

                return false;
            }
        }

        return true;
    }

    public void register(final Requirement requirement, final String type, final List<String> aliases, final boolean override) {
        final String requirementType = type == null && requirement.getType() == null ? requirement.getClass().getSimpleName() : type == null ? requirement.getType() : type;

        if (override) {
            requirements.put(requirementType, requirement);
        } else {
            requirements.putIfAbsent(requirementType, requirement);
        }

        if (aliases != null && aliases.size() > 0) {
            if (override) {
                aliases.forEach(alias -> requirements.put(alias, requirement));
            } else {
                aliases.forEach(alias -> requirements.putIfAbsent(alias, requirement));
            }
        }
    }

    public void register(final Requirement requirement, final String type, final List<String> aliases) {
        register(requirement, type, aliases, true);
    }

    public void register(final Requirement requirement, final String type, final boolean override) {
        register(requirement, type, requirement.getAliases(), override);
    }

    public void register(final Requirement requirement, final String type) {
        register(requirement, type, requirement.getAliases(), true);
    }

    public void register(final Requirement requirement, final boolean override) {
        register(requirement, requirement.getType(), requirement.getAliases(), override);
    }

    public void register(final Requirement requirement) {
        register(requirement, requirement.getType(), requirement.getAliases(), true);
    }

    public void executeDenyCommands(final Player player, final ConfigurationSection section) {
        if (denyCommandsHandler == null) {
            return;
        }

        denyCommandsHandler.execute(player, section.getStringList(denyCommandsSection));
    }

    public Requirement getRequirement(final String type) {
        return requirements.get(type);
    }

    public Map<String, Requirement> getRequirements() {
        return ImmutableMap.copyOf(requirements);
    }

    public String getDenyCommandsSection() {
        return denyCommandsSection;
    }

    public DenyCommandsHandler getDenyCommandsHandler() {
        return denyCommandsHandler;
    }

    public void setDenyCommandsHandler(DenyCommandsHandler denyCommandsHandler) {
        this.denyCommandsHandler = denyCommandsHandler;
    }

    public enum DenyCommandsExecution {
        EACH, FINAL, NONE
    }
}
