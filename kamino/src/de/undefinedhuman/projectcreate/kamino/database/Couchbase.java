package de.undefinedhuman.projectcreate.kamino.database;

import com.couchbase.client.core.env.LoggerConfig;
import com.couchbase.client.core.env.PasswordAuthenticator;
import com.couchbase.client.core.env.SecurityConfig;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ClusterOptions;
import com.couchbase.client.java.Scope;
import com.couchbase.client.java.env.ClusterEnvironment;
import de.undefinedhuman.projectcreate.engine.log.Log;

import java.nio.file.Path;

public class Couchbase implements Database {

    private final String connectionString;
    private final String bucketName;
    private final ClusterOptions options;
    private final ClusterEnvironment environment;
    private Cluster cluster;

    private Couchbase(String connectionString, ClusterOptions options, ClusterEnvironment environment, String bucketName) {
        this.connectionString = connectionString;
        this.options = options;
        this.environment = environment;
        this.bucketName = bucketName;
    }

    @Override
    public void init() {
        cluster = Cluster.connect(connectionString, options);
        Bucket bucket = cluster.bucket(bucketName);
        Scope scope = bucket.scope("meta");
    }

    public void ping() {
        if(cluster != null)
            Log.info(cluster.ping());
    }

    @Override
    public void close() {
        if(environment == null) return;
        environment.shutdown();
        if(cluster == null) return;
        cluster.disconnect();
    }

    public static class Builder {

        private final String connectionString;
        private final PasswordAuthenticator credentials;
        private SecurityConfig.Builder tlsConfig;
        private final String bucketName;

        public Builder(String connectionString, String username, String password, String bucketName) {
            this.connectionString = connectionString;
            this.credentials = PasswordAuthenticator.create(username, password);
            this.bucketName = bucketName;
        }

        public Builder setCertificate(Path path) {
            this.tlsConfig = SecurityConfig.builder()
                    .enableTls(true)
                    .trustCertificate(path);
            return this;
        }

        public Couchbase build() {
            ClusterOptions options = ClusterOptions.clusterOptions(credentials);
            ClusterEnvironment.Builder environmentConfig = ClusterEnvironment
                    .builder()
                    .loggerConfig(LoggerConfig
                            .customLogger(new CouchbaseLogger())
                            .fallbackToConsole(true)
                            .disableSlf4J(true)
                    );
            if(tlsConfig != null) environmentConfig.securityConfig(tlsConfig);
            ClusterEnvironment environment = environmentConfig.build();
            return new Couchbase(connectionString, options.environment(environment), environment, bucketName);
        }

    }

}
