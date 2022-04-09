package de.undefinedhuman.projectcreate.kamino.database;

public interface Database {
    void init();
    boolean saveMetadata(String id, String json);
    boolean saveEventData(String id, byte[] data);
    void close();
}
