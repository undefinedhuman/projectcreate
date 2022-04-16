package de.undefinedhuman.projectcreate.kamino.database;

import com.couchbase.client.core.diagnostics.ClusterState;
import com.couchbase.client.core.env.*;
import com.couchbase.client.core.error.*;
import com.couchbase.client.java.*;
import com.couchbase.client.java.codec.RawBinaryTranscoder;
import com.couchbase.client.java.codec.RawJsonTranscoder;
import com.couchbase.client.java.env.ClusterEnvironment;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.GetOptions;
import com.couchbase.client.java.kv.UpsertOptions;
import com.couchbase.client.java.manager.bucket.BucketSettings;
import com.couchbase.client.java.manager.collection.CollectionSpec;
import com.couchbase.client.java.manager.query.CreatePrimaryQueryIndexOptions;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryResult;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.ds.Tuple;
import de.undefinedhuman.projectcreate.server.ServerManager;

import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

public class Couchbase implements Database {

    private static final String SCOPE_NAME = "kamino";
    private static final String METADATA_COLLECTION_NAME = "meta";
    private static final String EVENT_DATA_COLLECTION_NAME = "data";

    private final String connectionString;
    private final String bucketName;
    private final Duration collectionTtlInDays;
    private final ClusterOptions options;
    private final ClusterEnvironment environment;
    private Cluster cluster;
    private Collection metaCollection, dataCollection;

    private Couchbase(String connectionString, ClusterOptions options, ClusterEnvironment environment, String bucketName, Duration collectionTtlInDays) {
        this.connectionString = connectionString;
        this.options = options;
        this.environment = environment;
        this.bucketName = bucketName;
        this.collectionTtlInDays = collectionTtlInDays;
    }

    @Override
    public void init() {
        Bucket bucket;
        Log.info("Establishing connection to couchbase cluster...");
        try {
            cluster = Cluster.connect(connectionString, options);
            cluster.waitUntilReady(Duration.ofSeconds(30));
            createBucket(cluster, bucketName);
            bucket = cluster.bucket(bucketName);
            bucket.waitUntilReady(Duration.ofSeconds(30));
            createScopes(bucket, SCOPE_NAME);
            createCollections(bucket, SCOPE_NAME, collectionTtlInDays, METADATA_COLLECTION_NAME, EVENT_DATA_COLLECTION_NAME);
            createPrimaryIndexForCollections(cluster, bucketName, SCOPE_NAME, METADATA_COLLECTION_NAME, EVENT_DATA_COLLECTION_NAME);
            Scope scope = bucket.scope(SCOPE_NAME);
            metaCollection = scope.collection(METADATA_COLLECTION_NAME);
            dataCollection = scope.collection(EVENT_DATA_COLLECTION_NAME);
            Log.info("Successfully established connection to couchbase cluster!");
        } catch(Exception ex) {
            Log.error("Couchbase connection failed. Shutting down server!", ex);
            ServerManager.getInstance().delete();
        }
    }

    private void createBucket(Cluster cluster, String bucketName) throws CouchbaseException {
        try {
            cluster.buckets().createBucket(BucketSettings.create(bucketName));
            Log.info("Successfully created bucket " + bucketName + "! It is however recommended to create it manually to properly set memory constraints!");
        } catch(BucketExistsException ex) {
            Log.debug("Bucket " + bucketName + " already exists!");
        }
    }

    private void createScopes(Bucket bucket, String... scopeNames) throws CouchbaseException {
        for(String scopeName : scopeNames) {
            try {
                bucket.collections().createScope(scopeName);
                Log.info("Successfully created scope " + scopeName + "!");
            } catch(ScopeExistsException ex) {
                Log.debug("Scope " + scopeName + " already exists!");
            }
        }
    }

    private void createCollections(Bucket bucket, String scopeName, Duration ttl, String... collectionNames) throws CouchbaseException {
        for(String collectionName : collectionNames) {
            try {
                CollectionSpec spec = CollectionSpec.create(collectionName, scopeName, ttl);
                bucket.collections().createCollection(spec);
                Log.info("Successfully created collection " + collectionName + "!");
            } catch (CollectionExistsException e) {
                Log.debug("Collection " + collectionName + " already exists!");
            } catch (ScopeNotFoundException ex) {
                Log.error("Scope " + scopeName + " does not exist!");
                throw ex;
            }
        }
    }

    private void createPrimaryIndexForCollections(Cluster cluster, String bucketName, String scopeName, String... collectionNames) throws CouchbaseException {
        for(String collectionName : collectionNames)
            try {
                CreatePrimaryQueryIndexOptions options = CreatePrimaryQueryIndexOptions
                        .createPrimaryQueryIndexOptions()
                        .scopeName(scopeName)
                        .collectionName(collectionName);
                cluster.queryIndexes().createPrimaryIndex(bucketName, options);
            } catch (IndexExistsException ex) {
                Log.debug("Primary index for collection " + collectionName + " in scope " + scopeName + " already exist!");
            } catch (IndexFailureException ex) {
                Log.error("Failed to create primary index for collection " + collectionName + " in scope " + scopeName + "!", ex);
            }
    }

    @Override
    public Tuple<String, Integer>[] searchMetadata(String whereQuery, Map<String, ?> parameters) {
        if(cluster == null || whereQuery == null || whereQuery.isEmpty() || parameters == null)
            return new Tuple[0];
        String selectQuery = "SELECT eventDataID, decompressedSize FROM " + bucketName + "." + SCOPE_NAME + "." + METADATA_COLLECTION_NAME + " AS kamino WHERE " + whereQuery;
        QueryResult result = cluster.query(selectQuery, QueryOptions.queryOptions().readonly(true).parameters(JsonObject.from(parameters)));
        return result.rowsAsObject()
                .stream()
                .filter(jsonObject -> jsonObject.containsKey("eventDataID") && jsonObject.containsKey("decompressedSize"))
                .map(jsonObject -> {
                    String id = jsonObject.getString("eventDataID");
                    Integer size = jsonObject.getInt("decompressedSize");
                    if(id == null || size == null || id.isBlank() || size < 0) return null;
                    return new Tuple<>(id, size);
                })
                .filter(Objects::nonNull)
                .toArray(Tuple[]::new);
    }

    @Override
    public byte[] searchEvent(String eventBucketID) {
        try {
            return dataCollection.get(eventBucketID, GetOptions.getOptions().transcoder(RawBinaryTranscoder.INSTANCE)).contentAsBytes();
        } catch (DocumentNotFoundException notFound) {
            Log.info("Event bucket with id " + eventBucketID + " does not exist!");
        } catch (CouchbaseException ex) {
            Log.error("Couchbase exception!");
        }
        return null;
    }

    @Override
    public String getTableName() {
        return "kamino.metadata";
    }

    @Override
    public boolean saveMetadata(String id, String json) {
        if(cluster.diagnostics().state() != ClusterState.ONLINE || metaCollection == null) return false;
        metaCollection.upsert(id, json, UpsertOptions.upsertOptions().transcoder(RawJsonTranscoder.INSTANCE));
        return true;
    }

    @Override
    public boolean saveEventData(String id, byte[] data) {
        if(cluster.diagnostics().state() != ClusterState.ONLINE || dataCollection == null) return false;
        dataCollection.upsert(id, data, UpsertOptions.upsertOptions().transcoder(RawBinaryTranscoder.INSTANCE));
        return true;
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
        private Duration collectionTtlInDays = Duration.ofDays(90);

        public Builder(String connectionString, String username, String password, String bucketName) {
            this.connectionString = connectionString;
            this.credentials = PasswordAuthenticator.create(username, password);
            this.bucketName = bucketName;
        }

        public Builder setDeleteAfter(Duration ttl) {
            if(ttl.compareTo(Duration.ofDays(1)) < 0) Log.warn("Collection TTL is recommended to be larger then 1 day! Proceed with caution!");
            this.collectionTtlInDays = ttl;
            return this;
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
                    .loggerConfig(new LoggerConfig.Builder().fallbackToConsole(true).disableSlf4J(true))
                    .orphanReporterConfig(new OrphanReporterConfig.Builder().emitInterval(Duration.ofHours(1)))
                    .timeoutConfig(new TimeoutConfig.Builder().kvTimeout(Duration.ofSeconds(10)));
            environmentConfig.orphanReporterConfig(new OrphanReporterConfig.Builder().emitInterval(Duration.ofHours(1)));
            if(tlsConfig != null) environmentConfig.securityConfig(tlsConfig);
            ClusterEnvironment environment = environmentConfig.build();
            return new Couchbase(connectionString, options.environment(environment), environment, bucketName, collectionTtlInDays);
        }

    }

}