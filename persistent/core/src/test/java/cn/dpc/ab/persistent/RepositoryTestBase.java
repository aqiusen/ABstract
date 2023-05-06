package cn.dpc.ab.persistent;

import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.couchbase.CouchbaseContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.awaitility.Awaitility;

/**
 * Base class for repository tests.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = TestConfiguration.class)
@Testcontainers
@ActiveProfiles("test")
public class RepositoryTestBase {
    @ClassRule
    @Container
    static final CouchbaseContainer couchbaseContainer = CouchbaseTestContainer.getInstance();

    @BeforeAll
    public static void setup() {
        couchbaseContainer.start();
        Awaitility.await().until(couchbaseContainer::isRunning);
    }
}
