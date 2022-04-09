package de.undefinedhuman.projectcreate.kamino.config;

import de.undefinedhuman.projectcreate.engine.config.Config;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.StringSetting;

public class KaminoConfig extends Config {

    private static volatile KaminoConfig instance;

    public static final int DEFAULT_NUMBER_OF_THREADS = 1;

    private static final KaminoConfigValidator VALIDATOR = new KaminoConfigValidator();

    public final StringSetting databaseUrl = new StringSetting("DATABASE_URL", "127.0.0.1");
    public final StringSetting databaseUser = new StringSetting("DATABASE_USER", "couchbaseUser");
    public final StringSetting databasePassword = new StringSetting("DATABASE_PASSWORD", "V3ryS3cur3P4ssw0rd");
    public final StringSetting bucketName = new StringSetting("DATABASE_BUCKET_NAME", "projectcreate");
    public final StringSetting scopeName = new StringSetting("DATABASE_SCOPE_NAME", "kamino");
    public final IntSetting numberOfThreads = new IntSetting("NUMBER_OF_THREADS", DEFAULT_NUMBER_OF_THREADS);

    private KaminoConfig() {
        super("kamino/kamino");
        addSettings(databaseUrl, databaseUser, databasePassword, bucketName, scopeName, numberOfThreads);
    }

    @Override
    public void validate() {
        VALIDATOR.validate(this);
    }

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