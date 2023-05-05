package cn.dpc.ab.persistent;

import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.couchbase.BucketDefinition;
import org.testcontainers.couchbase.CouchbaseContainer;

import java.time.Duration;

public class CouchbaseTestContainer extends CouchbaseContainer {
    private static CouchbaseTestContainer container;

    private static final String COUCHBASE_VERSION = "community-7.1.1";
    public static final String BUCKET_NAME = "testBucket";
    public static final String USERNAME = "testUser";
    public static final String PASSWORD = "testPassword";

    private CouchbaseTestContainer() {
        super("couchbase/server:" + COUCHBASE_VERSION);
        addEnv("SERVICES", "data,index,query");
        addEnv("CLUSTER_RAMSIZE", "256");
        addEnv("USERNAME", USERNAME);
        addEnv("PASSWORD", PASSWORD);
        addEnv("BUCKET_NAME", BUCKET_NAME);
        withBucket(new BucketDefinition(BUCKET_NAME).withPrimaryIndex(true));
        withCredentials(USERNAME, PASSWORD);
        withExposedPorts(8091, 8092, 8093, 8094, 8095, 8096);
        withStartupTimeout(Duration.ofSeconds(90));
        waitingFor(Wait.forHealthcheck());
    }

    public static CouchbaseTestContainer getInstance() {
        if (container == null) {
            container = new CouchbaseTestContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("BUCKET_NAME", BUCKET_NAME);
        System.setProperty("USERNAME", container.getUsername());
        System.setProperty("PASSWORD", container.getPassword());
        System.setProperty("CONNECTION_STRING", container.getConnectionString());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}