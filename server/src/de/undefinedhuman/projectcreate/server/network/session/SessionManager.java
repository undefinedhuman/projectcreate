package de.undefinedhuman.projectcreate.server.network.session;

import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.manager.Manager;

import java.util.HashMap;

public class SessionManager implements Manager {

    private static volatile SessionManager instance;

    private final HashMap<String, Long> worldIDs = new HashMap<>();

    private SessionManager() {}

    public void registerSession(String sessionID) {
        this.worldIDs.put(sessionID, -1L);
    }

    public void setWorldID(String sessionID, long worldID) {
        this.worldIDs.put(sessionID, worldID);
    }

    public Long getWorldID(String sessionID) {
        Long worldID = worldIDs.get(sessionID);
        if(worldID == null)
            Log.warn("No world id associated with session!");
        return worldIDs.get(sessionID);
    }

    public static SessionManager getInstance() {
        if(instance != null)
            return instance;
        synchronized (SessionManager.class) {
            if (instance == null)
                instance = new SessionManager();
        }
        return instance;
    }

}
