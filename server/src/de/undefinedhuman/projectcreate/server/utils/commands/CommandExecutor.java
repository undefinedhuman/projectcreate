package de.undefinedhuman.projectcreate.server.utils.commands;

@FunctionalInterface
public interface CommandExecutor {
    boolean execute(CommandSender sender, Command command, String label, String... args);
}
