package de.undefinedhuman.projectcreate.kamino.database;

import com.couchbase.client.core.env.OrphanReporterConfig;
import com.couchbase.client.core.env.PasswordAuthenticator;
import com.couchbase.client.core.env.SecurityConfig;
import com.couchbase.client.core.env.TimeoutConfig;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ClusterOptions;
import com.couchbase.client.java.Scope;
import com.couchbase.client.java.env.ClusterEnvironment;
import de.undefinedhuman.projectcreate.engine.log.Log;

import java.nio.file.Path;
import java.time.Duration;

public class Couchbase implements Database {

    private final String connectionString;
    private final String bucketName;
    private final ClusterOptions options;
    private ClusterEnvironment environment;
    private Cluster cluster;

    private Couchbase(String connectionString, ClusterOptions options, ClusterEnvironment environment, String bucketName) {
        this.connectionString = connectionString;
        this.options = options;
        this.environment = environment;
        this.bucketName = bucketName;
    }

    @Override
    public void init() {
        try {
            cluster = Cluster.connect(connectionString, options);
            cluster.waitUntilReady(Duration.ofSeconds(5));
            Bucket bucket = cluster.bucket(bucketName);
            bucket.waitUntilReady(Duration.ofSeconds(5));
            Scope scope = bucket.scope("meta");
        } catch(Exception ex) {
            cluster.disconnect();
            environment.shutdown();
        }
//        cluster.waitUntilReady(Duration.ofSeconds(5));
////        Bucket bucket = cluster.bucket(bucketName);
////        Scope scope = bucket.scope("meta");
//        Log.info("HELLO", cluster.diagnostics().state());
//        if(cluster.diagnostics().state() == ClusterState.OFFLINE)
//            cluster.disconnect();
    }

    public void ping() {
        if(cluster != null)
            Log.info(cluster.diagnostics().state());
    }

    @Override
    public void close() {
        if(cluster != null) cluster.disconnect(Duration.ofSeconds(30));
        if(environment != null) environment.shutdown();
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
                    .timeoutConfig(new TimeoutConfig.Builder().kvTimeout(Duration.ofSeconds(10)));
            environmentConfig.orphanReporterConfig(new OrphanReporterConfig.Builder().emitInterval(Duration.ofHours(1)));
            if(tlsConfig != null) environmentConfig.securityConfig(tlsConfig);
            ClusterEnvironment environment = environmentConfig.build();
            return new Couchbase(connectionString, options.environment(environment), environment, bucketName);
        }

    }

}
