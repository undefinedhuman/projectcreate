package de.undefinedhuman.projectcreate.server.utils.commands;

import java.util.List;

public class Command {

    private String label;
    private List<String> aliases;
    private String description;
    private CommandExecutor commandExecutor;

    public CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }
}
