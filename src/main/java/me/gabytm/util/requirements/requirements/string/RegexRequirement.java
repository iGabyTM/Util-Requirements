package me.gabytm.util.requirements.requirements.string;

import me.gabytm.util.requirements.DefaultRequirements;
import me.gabytm.util.requirements.Requirement;
import me.gabytm.util.requirements.RequirementsManager;
import me.gabytm.util.requirements.utils.StringUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexRequirement implements Requirement {
    private final RequirementsManager manager;
    private final boolean caseInsensitive;

    public RegexRequirement(RequirementsManager manager, boolean caseInsensitive) {
        this.manager = manager;
        this.caseInsensitive = caseInsensitive;
    }

    @Override
    public String getType() {
        return caseInsensitive ? DefaultRequirements.REGEX_CASE_INSENSITIVE.getType() : DefaultRequirements.REGEX.getType();
    }

    @Override
    public boolean eval(Player player, ConfigurationSection section, RequirementsManager.DenyCommandsExecution denyCommandsExecution) {
        final String input = StringUtil.replacePlaceholders(player, section.getString("input"));
        final String regex = StringUtil.replacePlaceholders(player, section.getString("regex"));

        if (input == null || regex == null) {
            if (denyCommandsExecution == RequirementsManager.DenyCommandsExecution.EACH) {
                manager.executeDenyCommands(player, section);
            }

            return false;
        }

        final Pattern pattern = Pattern.compile(regex, caseInsensitive ? Pattern.CASE_INSENSITIVE : 0);
        final Matcher matcher = pattern.matcher(input);
        final boolean result = matcher.find();

        if (!result && denyCommandsExecution == RequirementsManager.DenyCommandsExecution.EACH) {
            manager.executeDenyCommands(player, section);
        }

        return result;
    }
}
