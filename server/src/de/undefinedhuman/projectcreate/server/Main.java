package de.undefinedhuman.projectcreate.server;

import de.undefinedhuman.projectcreate.engine.log.Log;

public class Main {

    public static void main(String[] args) {
        Log.instance = new Log();
        Log.info("SERVER STARTED!");
        Log.info("SERVER STOPPED!");
        Log.instance.save();
        int i = 500;
        while(i > 0) {
            try {
                Thread.sleep(1000);
                i--;
            } catch (InterruptedException e) {
                Log.error("Error in server thread", e);
                Thread.currentThread().interrupt();
            }
        }
    }

}
