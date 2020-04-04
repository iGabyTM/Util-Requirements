package me.gabytm.util.requirements;

import org.bukkit.entity.Player;

import java.util.List;

@FunctionalInterface
public interface DenyCommandsHandler {
    void execute(final Player player, final List<String> commands);
}
