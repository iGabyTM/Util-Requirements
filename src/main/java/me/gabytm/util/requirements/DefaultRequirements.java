package me.gabytm.util.requirements;

import java.util.List;

public enum DefaultRequirements {
    HAS_PERMISSION("has permission", null),
    HAS_PERMISSION_REVERSED("!has permission", null),

    REGEX("regex", null),
    REGEX_CASE_INSENSITIVE("case insensitive regex", null),

    IO_EQUAL_TO("==", null),
    IO_GREATER_THAN(">", null),
    IO_GREATER_THAN_OR_EQUAL_TO(">=", null),
    IO_LESS_THAN("<", null),
    IO_LESS_THAN_OR_EQUAL_TO("<=", null),
    IO_STRING_CONTAINS("string contains", null),
    IO_STRING_EQUALS("string equals", null),
    IO_STRING_EQUALS_IGNORE_CASE("string equals ignore case", null);

    private final String type;
    private final List<String> aliases;

    DefaultRequirements(String type, List<String> aliases) {
        this.type = type;
        this.aliases = aliases;
    }

    public String getType() {
        return type;
    }

    public List<String> getAliases() {
        return aliases;
    }
}
