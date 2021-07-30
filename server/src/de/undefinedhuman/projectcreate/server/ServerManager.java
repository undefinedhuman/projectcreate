package de.undefinedhuman.projectcreate.server;

import de.undefinedhuman.projectcreate.engine.gl.HeadlessApplicationListener;

public class ServerManager extends HeadlessApplicationListener {

    private static volatile ServerManager instance;

    public ServerManager() {

    }

    @Override
    public void create() {

    }

    @Override
    public void dispose() {

    }

    public static ServerManager getInstance() {
        if(instance != null)
            return instance;
        synchronized (ServerManager.class) {
            if (instance == null)
                instance = new ServerManager();
        }
        return instance;
    }

}
