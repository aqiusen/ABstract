package cn.dpc.ab.persistent.feature;

import cn.dpc.ab.domain.exception.RecordNotFoundException;
import cn.dpc.ab.domain.feature.FeatureFlag;
import cn.dpc.ab.domain.feature.FeatureFlags;
import cn.dpc.ab.domain.feature.FeatureFlags.FilterParams;
import cn.dpc.ab.persistent.feature.db.FeatureFlagDb;
import cn.dpc.ab.persistent.feature.db.FeatureFlagDbRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static cn.dpc.ab.persistent.feature.FeatureFlagTestUtil.createFeatureFlag;
import static cn.dpc.ab.persistent.feature.FeatureFlagTestUtil.createFeatureFlagDb;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeatureFlagsImplTest {
    @Mock
    FeatureFlagDbRepository repository;

    FeatureFlags featureFlags;

    @BeforeEach
    void setUp() {
        featureFlags = new FeatureFlagsImpl(repository);
    }

    @Test
    public void should_add_success() {
        FeatureFlag featureFlag = createFeatureFlag("featureKey1");

        when(repository.save(any(FeatureFlagDb.class))).thenAnswer(invocation -> {
            FeatureFlagDb featureFlagDb = invocation.getArgument(0);
            return Mono.just(featureFlagDb);
        });

        featureFlags.add(featureFlag)
                .as(StepVerifier::create)
                .expectNextMatches(config -> config.getId().featureKey().equals("featureKey1")
                        && config.getDescription().updatedAt() != null
                        && config.getDescription().createdAt() != null
                )
                .verifyComplete();
    }

    @Test
    public void should_add_return_error_when_repository_save_error() {
        FeatureFlag featureFlag = createFeatureFlag("featureKey1");

        when(repository.save(any(FeatureFlagDb.class))).thenReturn(Mono.error(new DuplicateKeyException("save error")));

        featureFlags.add(featureFlag)
                .as(StepVerifier::create)
                .expectError()
                .verify();
    }

    @Test
    public void should_update_success() {
        String featureKey = "featureKey1";
        FeatureFlag featureFlag = createFeatureFlag(featureKey);
        FeatureFlagDb db = FeatureFlagDb.from(featureFlag);
        FeatureFlag updatedFeatureFlag = createFeatureFlag(featureKey).update(FeatureFlag.FeatureFlagDescription.builder()
                        .description("description1")
                        .name("name1")
                        .enabled(true)
                .build());

        when(repository.findById(anyString())).thenReturn(Mono.just(db));
        when(repository.save(any(FeatureFlagDb.class))).thenAnswer(invocation -> {
            FeatureFlagDb featureFlagDb = invocation.getArgument(0);
            return Mono.just(featureFlagDb);
        });

        featureFlags.update(updatedFeatureFlag.getId(), updatedFeatureFlag.getDescription())
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    public void should_update_throw_error_when_featureFlag_not_found() {
        String featureKey = "not_exist";
        when(repository.findById(anyString())).thenReturn(Mono.empty());

        FeatureFlag updateFeatureFlag = createFeatureFlag(featureKey);

        featureFlags.update(updateFeatureFlag.getId(), updateFeatureFlag.getDescription())
                .as(StepVerifier::create)
                .expectError(RecordNotFoundException.class)
                .verify();
    }

    @Test
    public void should_enable_success() {
        String featureKey = "featureKey1";
        FeatureFlag featureFlag = createFeatureFlag(featureKey);
        FeatureFlagDb db = FeatureFlagDb.from(featureFlag);

        when(repository.findById(anyString())).thenReturn(Mono.just(db));
        when(repository.save(any(FeatureFlagDb.class))).thenAnswer(invocation -> {
            FeatureFlagDb featureFlagDb = invocation.getArgument(0);
            return Mono.just(featureFlagDb);
        });

        featureFlags.enable(new FeatureFlag.FeatureFlagId(featureKey))
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    public void should_enable_return_error_when_featureFlag_not_exist() {
        String featureKey = "not_exist";
        when(repository.findById(anyString())).thenReturn(Mono.empty());

        featureFlags.enable(new FeatureFlag.FeatureFlagId(featureKey))
                .as(StepVerifier::create)
                .expectError(RecordNotFoundException.class)
                .verify();
    }

    @Test
    public void should_disable_success() {
        String featureKey = "featureKey1";
        FeatureFlag featureFlag = createFeatureFlag(featureKey);
        FeatureFlagDb db = FeatureFlagDb.from(featureFlag);

        when(repository.findById(anyString())).thenReturn(Mono.just(db));
        when(repository.save(any(FeatureFlagDb.class))).thenAnswer(invocation -> {
            FeatureFlagDb featureFlagDb = invocation.getArgument(0);
            return Mono.just(featureFlagDb);
        });

        featureFlags.disable(new FeatureFlag.FeatureFlagId(featureKey))
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    public void should_disable_return_error_when_featureFlag_not_exist() {
        String featureKey = "not_exist";
        when(repository.findById(anyString())).thenReturn(Mono.empty());

        featureFlags.disable(new FeatureFlag.FeatureFlagId(featureKey))
                .as(StepVerifier::create)
                .expectError(RecordNotFoundException.class)
                .verify();
    }

    @Test
    public void should_get_all_success() {
        when(repository.findAll()).thenReturn(Flux.just(FeatureFlagTestUtil.createFeatureFlagDb("featureKey1"),
                FeatureFlagTestUtil.createFeatureFlagDb("featureKey2"),
                FeatureFlagTestUtil.createFeatureFlagDb("featureKey3")));

        featureFlags.getAll(FilterParams.builder().key("3").build())
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void should_get_all_return_empty_when_no_record() {
        when(repository.findAll()).thenReturn(Flux.empty());

        featureFlags.getAll(FilterParams.builder().key("3").build())
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void should_get_success() {
        String featureKey = "featureKey1";
        when(repository.findById(anyString())).thenReturn(Mono.just(FeatureFlagTestUtil.createFeatureFlagDb(featureKey)));

        featureFlags.get(new FeatureFlag.FeatureFlagId(featureKey))
                .as(StepVerifier::create)
                .expectNextMatches(config -> config.getId().featureKey().equals(featureKey))
                .verifyComplete();
    }

    @Test
    public void should_get_return_empty_when_id_not_exist() {
        String featureKey = "not_exist";
        when(repository.findById(anyString())).thenReturn(Mono.empty());

        featureFlags.get(new FeatureFlag.FeatureFlagId(featureKey))
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void should_subCollection_return_empty_when_no_record() {
        when(repository.findAll()).thenReturn(Flux.empty());

        featureFlags.subCollection(FilterParams.builder().build(), 0, 10)
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void should_subCollection_return_success() {
        when(repository.findAll()).thenReturn(Flux.just(FeatureFlagTestUtil.createFeatureFlagDb("featureKey1"),
                FeatureFlagTestUtil.createFeatureFlagDb("featureKey2"),
                FeatureFlagTestUtil.createFeatureFlagDb("featureKey3")));

        featureFlags.subCollection(FilterParams.builder().build(), 0, 2)
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void should_subCollection_return_success_when_filter_by_key() {
        when(repository.findAll()).thenReturn(Flux.just(FeatureFlagTestUtil.createFeatureFlagDb("featureKey1"),
                FeatureFlagTestUtil.createFeatureFlagDb("featureKey2"),
                FeatureFlagTestUtil.createFeatureFlagDb("featureKey3")));

        featureFlags.subCollection(FilterParams.builder().key("2").build(), 0, 2)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void should_size_return_0_when_no_record() {
        when(repository.findAll()).thenReturn(Flux.empty());

        featureFlags.size(FilterParams.builder().build())
                .as(StepVerifier::create)
                .expectNext(0L)
                .verifyComplete();
    }

    @Test
    public void should_size_return_size_number() {
        when(repository.findAll()).thenReturn(Flux.just(FeatureFlagTestUtil.createFeatureFlagDb("featureKey1"),
                FeatureFlagTestUtil.createFeatureFlagDb("featureKey2"),
                FeatureFlagTestUtil.createFeatureFlagDb("featureKey3")));

        featureFlags.size(FilterParams.builder().build())
                .as(StepVerifier::create)
                .expectNext(3L)
                .verifyComplete();
    }

    @Test
    public void should_size_return_size_number_with_filter_params() {
        when(repository.findAll()).thenReturn(Flux.just(FeatureFlagTestUtil.createFeatureFlagDb("featureKey1"),
                FeatureFlagTestUtil.createFeatureFlagDb("featureKey2"),
                FeatureFlagTestUtil.createFeatureFlagDb("featureKey3")));

        featureFlags.size(FilterParams.builder().key("3").build())
                .as(StepVerifier::create)
                .expectNext(1L)
                .verifyComplete();
    }


    @Test
    public void should_enabled_success() {
        when(repository.findAll()).thenReturn(Flux.just(createFeatureFlagDb("featureKey1", true),
                FeatureFlagTestUtil.createFeatureFlagDb("featureKey2"),
                createFeatureFlagDb("featureKey3", true)));

        featureFlags.enabled(FilterParams.builder().build())
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void should_enabled_return_empty_when_no_record() {
        when(repository.findAll()).thenReturn(Flux.empty());

        featureFlags.enabled(FilterParams.builder().build())
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void should_enabled_return_with_filter() {
        when(repository.findAll()).thenReturn(Flux.just(createFeatureFlagDb("featureKey1", true),
                FeatureFlagTestUtil.createFeatureFlagDb("featureKey2"),
                createFeatureFlagDb("featureKey3", true)));

        featureFlags.enabled(FilterParams.builder().key("3").build())
                .as(StepVerifier::create)
                .expectNextMatches(config -> config.getId().featureKey().equals("featureKey3"))
                .verifyComplete();
    }

}