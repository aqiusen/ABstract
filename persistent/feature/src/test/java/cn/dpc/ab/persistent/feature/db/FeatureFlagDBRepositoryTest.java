package cn.dpc.ab.persistent.feature.db;

import cn.dpc.ab.persistent.RepositoryTestBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static cn.dpc.ab.persistent.feature.FeatureFlagTestUtil.createFeatureFlagDB;


class FeatureFlagDBRepositoryTest extends RepositoryTestBase {
    @Autowired
    FeatureFlagDBRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll().block();
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Test
    public void should_save_FeatureFlag_success() {
        var featureKey = "featureKey1";
        repository.save(createFeatureFlagDB(featureKey))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void should_add_same_featureKey_fail() {
        var featureKey = "featureKey1";
        repository.save(createFeatureFlagDB(featureKey)).block();

        repository.save(createFeatureFlagDB(featureKey))
                .as(StepVerifier::create)
                .expectError()
                .verify();
    }

    @Test
    public void should_update_same_featureKey_fail() {
        var featureKey = "featureKey1";
        FeatureFlagDB db = repository.save(createFeatureFlagDB(featureKey)).block();

        FeatureFlagDB db1 = FeatureFlagDB.from(db.to().enable());
        db1.setVersion(db.getVersion());

        repository.save(db1)
                .as(StepVerifier::create)
                .expectNextMatches(config -> config.getDescription().enabled())
                .verifyComplete();
    }

    @Test
    public void should_getById_success() {
        var featureKey = "featureKey1";
        repository.save(createFeatureFlagDB(featureKey)).block();

        repository.findById(FeatureFlagDB.idFromFeatureKey(featureKey))
                .as(StepVerifier::create)
                .expectNextMatches(config -> config.getFeatureKey().equals(featureKey))
                .verifyComplete();
    }

    @Test
    public void should_findAll_success() {
        repository.save(createFeatureFlagDB("key1")).block();
        repository.save(createFeatureFlagDB("key2")).block();
        repository.save(createFeatureFlagDB("key3")).block();

        repository.findAll()
                .as(StepVerifier::create)
                .expectNextCount(3)
                .verifyComplete();
    }

}