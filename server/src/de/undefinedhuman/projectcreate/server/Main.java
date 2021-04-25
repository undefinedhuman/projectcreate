package de.undefinedhuman.projectcreate.server;

import de.undefinedhuman.projectcreate.engine.log.Log;

public class Main {

    public static void main(String[] args) {
        int i = 500;
        while(i > 0) {
            try {
                Thread.sleep(1000);
                System.out.println("Server still running!");
                i--;
            } catch (InterruptedException e) {
                Log.error("Error while server sleep" + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("SERVER STOPPED!");
    }

}
