package de.undefinedhuman.projectcreate.server.utils.console;

import de.undefinedhuman.projectcreate.engine.utils.manager.Manager;
import de.undefinedhuman.projectcreate.server.utils.commands.CommandManager;
import de.undefinedhuman.projectcreate.server.utils.commands.CommandSender;

import java.util.Arrays;
import java.util.Scanner;

public class Console implements Manager {

    private static volatile Console instance;

    private Scanner input;
    private Thread consoleListener;

    private Console() {
        input = new Scanner(System.in);
        consoleListener = new Thread(() -> {
            while(true) {
                input.hasNext();
                processInput();
            }
        });
    }

    @Override
    public void init() {
        this.consoleListener.start();
    }

    @Override
    public void delete() {
        consoleListener.interrupt();
        input.close();
        input = null;
    }

    private void processInput() {
        if(input == null)
            return;
        String input = this.input.nextLine();
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
