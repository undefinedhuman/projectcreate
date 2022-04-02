package de.undefinedhuman.projectcreate.kamino.config;

import de.undefinedhuman.projectcreate.engine.config.Config;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.StringSetting;

public class KaminoConfig extends Config {

    private static volatile KaminoConfig instance;

    public final StringSetting databaseUrl = new StringSetting("DATABASE_URL", "127.0.0.1");
    public final StringSetting databaseUser = new StringSetting("DATABASE_USER", "couchbaseUser");
    public final StringSetting databasePassword = new StringSetting("DATABASE_PASSWORD", "V3ryS3cur3P4ssw0rd");
    public final StringSetting tableName = new StringSetting("DATABASE_TABLE_NAME", "kamino");

    private KaminoConfig() {
        super("kamino/kamino");
        addSettings(databaseUrl, databaseUser, databasePassword, tableName);
    }

    @Override
    public void validate() {}

    public static KaminoConfig getInstance() {
        if(instance != null)
            return instance;
        synchronized (KaminoConfig.class) {
            if (instance == null)
                instance = new KaminoConfig();
        }
        return instance;
    }

}