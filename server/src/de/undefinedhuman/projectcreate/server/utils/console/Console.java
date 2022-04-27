package de.undefinedhuman.projectcreate.server.utils.console;

import de.undefinedhuman.projectcreate.engine.utils.manager.Manager;
import de.undefinedhuman.projectcreate.server.utils.commands.CommandManager;
import de.undefinedhuman.projectcreate.server.utils.commands.CommandSender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Console implements Manager {

    private static volatile Console instance;

    private final BufferedReader consoleReader;

    private Console() {
        consoleReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void update(float delta) {
        try {
            if(!consoleReader.ready()) return;
            processInput(consoleReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete() {
        try {
            consoleReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processInput(String input) {
        String[] commandData = input.split(" ");
        CommandManager.getInstance().executeCommand(CommandSender.CONSOLE, commandData[0], Arrays.copyOfRange(commandData, 1, commandData.length));
    }

    public static Console getInstance() {
        if(instance != null)
            return instance;
        synchronized (Console.class) {
            if (instance == null)
                instance = new Console();
        }
        return instance;
    }

}
