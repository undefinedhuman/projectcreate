package de.undefinedhuman.sandboxgameserver;

import java.util.Scanner;

public class Main {

    public static boolean terminated = false, closing = false;

    public static void main(String[] args) {

        ServerManager.instance = new ServerManager();

        final Scanner scan = new Scanner(System.in);
        boolean exit = false;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

            closing = true;
            scan.close();
            if (!terminated) ServerManager.instance.closeServer();

        }));

        while (!exit) {

            String str = scan.nextLine();
            if (str.equals("exit") || str.equals("quit") || str.equals("stop")) {
                exit = true;
                closing = true;
            }

        }

        scan.close();
        ServerManager.instance.closeServer();
        terminated = true;

        System.exit(0);

    }

}
