package de.undefinedhuman.projectcreate.server;

import com.badlogic.gdx.Files;
import de.undefinedhuman.projectcreate.engine.log.Log;

public class Main {

    public static void main(String[] args) {
        Log.instance = new Log();
        Log.instance.type = Files.FileType.Local;
        Log.instance.init();
        Log.instance.load();
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
