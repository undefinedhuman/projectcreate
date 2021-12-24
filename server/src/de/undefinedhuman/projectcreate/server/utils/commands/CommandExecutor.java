package de.undefinedhuman.projectcreate.server.utils.commands;

@FunctionalInterface
public interface CommandExecutor {
    boolean execute(CommandSender sender, String label, String... args);
}
