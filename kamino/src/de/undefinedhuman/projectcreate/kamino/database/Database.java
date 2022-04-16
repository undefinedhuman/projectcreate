package de.undefinedhuman.projectcreate.kamino.database;

import de.undefinedhuman.projectcreate.engine.utils.ds.Tuple;

import java.util.Map;

public interface Database {
    void init();
    boolean saveMetadata(String id, String json);
    boolean saveEventData(String id, byte[] data);
    Tuple<String, Integer>[] searchMetadata(String query, Map<String, ?> parameters);
    byte[] searchEvent(String eventBucketID);
    String getTableName();
    void close();
}
