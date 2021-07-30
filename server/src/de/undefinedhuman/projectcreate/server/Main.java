package de.undefinedhuman.projectcreate.server;

import java.util.Scanner;

public class Main {

    private static boolean isTerminated = false, isClosing = false;

    public static void main(String[] args) {
        ServerManager.getInstance();

        final Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            isClosing = true;
            terminateServer(scanner);
        }));

        while (!exit) {
            String str = scanner.nextLine();
            if (str.equals("exit") || str.equals("quit") || str.equals("stop")) { exit = true; isClosing = true; }
        }

        terminateServer(scanner);
        System.exit(0);
    }

    private static void terminateServer(Scanner scanner) {
        scanner.close();
        if(!isTerminated) ServerManager.getInstance().dispose();
        isTerminated = true;
    }

    public static boolean isClosing() {
        return isClosing;
    }

}
