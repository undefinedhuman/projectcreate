package de.undefinedhuman.projectcreate.server;

import de.undefinedhuman.projectcreate.engine.log.Log;

public class Main {

    public static void main(String[] args) {
        Log.instance = new Log();
        Log.instance.init();
        Log.instance.load();
        Log.info("SERVER STARTED!");
        Log.info("SERVER STOPPED!");
        Log.instance.save();
    }

}
