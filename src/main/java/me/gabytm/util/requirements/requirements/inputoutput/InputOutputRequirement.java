package me.gabytm.util.requirements.requirements.inputoutput;

import com.google.common.primitives.Doubles;
import me.gabytm.util.requirements.DefaultRequirements;
import me.gabytm.util.requirements.Requirement;
import me.gabytm.util.requirements.RequirementsManager;
import me.gabytm.util.requirements.utils.StringUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class InputOutputRequirement implements Requirement {
    private final RequirementsManager manager;
    private final DefaultRequirements type;

    public InputOutputRequirement(RequirementsManager manager, DefaultRequirements type) {
        this.manager = manager;
        this.type = type;
    }

    @Override
    public String getType() {
        return type.getType();
    }

    @Override
    public boolean eval(Player player, ConfigurationSection section, RequirementsManager.DenyCommandsExecution denyCommandsExecution) {
        final String input = StringUtil.replacePlaceholders(player, section.getString("input"));
        final String output = StringUtil.replacePlaceholders(player, section.getString("output"));
        boolean result = false;

        switch (type) {
            case IO_STRING_CONTAINS: {
                result = input.contains(output);
                break;
            }

            case IO_STRING_EQUALS: {
                result = input.equals(output);
                break;
            }

            case IO_STRING_EQUALS_IGNORE_CASE: {
                result = input.equalsIgnoreCase(output);
                break;
            }

            default: {
                break;
            }
        }

        final Double inputNumber = Doubles.tryParse(input);
        final Double outputNumber = Doubles.tryParse(output);

        if (inputNumber == null || outputNumber == null) {
            if (denyCommandsExecution == RequirementsManager.DenyCommandsExecution.EACH) {
                manager.executeDenyCommands(player, section);
            }

            return false;
        }

        switch (type) {
            case IO_EQUAL_TO: {
                result = inputNumber.equals(outputNumber);
                break;
            }

            case IO_GREATER_THAN: {
                result = inputNumber > outputNumber;
                break;
            }

            case IO_GREATER_THAN_OR_EQUAL_TO: {
                result = inputNumber >= outputNumber;
                break;
            }

            case IO_LESS_THAN: {
                result = inputNumber < outputNumber;
                break;
            }

            case IO_LESS_THAN_OR_EQUAL_TO: {
                result = inputNumber <= outputNumber;
                break;
            }

            default: {
                break;
            }
        }

        if (!result && denyCommandsExecution == RequirementsManager.DenyCommandsExecution.EACH) {
            manager.executeDenyCommands(player, section);
        }

        return result;
    }
}
