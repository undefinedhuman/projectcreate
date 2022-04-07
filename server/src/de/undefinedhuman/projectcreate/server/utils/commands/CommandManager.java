package de.undefinedhuman.projectcreate.server.utils.commands;

import de.undefinedhuman.projectcreate.engine.utils.manager.Manager;

import java.util.HashMap;

public class CommandManager implements Manager {

    private static volatile CommandManager instance;

    private HashMap<String, Command> commands;

    private CommandManager() {
        this.commands = new HashMap<>();
    }

    public CommandManager addCommand(String name, Command command) {
        this.commands.put(name, command);
        return instance;
    }

    public boolean executeCommand(CommandSender sender, String label, String... args) {
        Command command = this.commands.get(label);
        if(command == null)
            return false;
        CommandExecutor executor = this.commands.get(label).getCommandExecutor();
        if(executor == null)
            return false;
        return executor.execute(sender, command, label, args);
    }

    @Override
    public void delete() {
        this.commands.clear();
    }

    public static CommandManager getInstance() {
        if(instance != null)
            return instance;
        synchronized (CommandManager.class) {
            if (instance == null)
                instance = new CommandManager();
        }
        return instance;
    }

}
