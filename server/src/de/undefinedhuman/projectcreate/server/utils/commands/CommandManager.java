package de.undefinedhuman.projectcreate.server.utils.commands;

import de.undefinedhuman.projectcreate.engine.utils.manager.Manager;

import java.util.HashMap;

public class CommandManager extends Manager {

    private static volatile CommandManager instance;

    private HashMap<String, CommandExecutor> commands;

    private CommandManager() {
        this.commands = new HashMap<>();
    }

    public CommandManager addCommand(String name, CommandExecutor executor) {
        this.commands.put(name, executor);
        return instance;
    }

    public boolean executeCommand(CommandSender sender, String label, String... args) {
        CommandExecutor executor = this.commands.get(label);
        if(executor == null)
            return false;
        return executor.execute(sender, label, args);
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
