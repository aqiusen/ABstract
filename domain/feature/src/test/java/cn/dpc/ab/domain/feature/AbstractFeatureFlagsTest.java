package cn.dpc.ab.domain.feature;

import cn.dpc.ab.domain.exception.RecordNotFoundException;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class AbstractFeatureFlagsTest {

    @Test
    public void should_update_success() {
        FeatureFlags featureFlags = new MockSuccessFeatureFlags();
        FeatureFlag.FeatureFlagId id = new FeatureFlag.FeatureFlagId("featureKey");
        FeatureFlag.FeatureFlagDescription description = FeatureFlag.FeatureFlagDescription.builder()
                .name("name")
                .description("description")
                .build();

        featureFlags.update(id, description)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    public void should_update_return_error_when_id_not_exist() {
        FeatureFlags featureFlags = new MockNotFoundFeatureFlags();
        FeatureFlag.FeatureFlagId id = new FeatureFlag.FeatureFlagId("featureKey");
        FeatureFlag.FeatureFlagDescription description = FeatureFlag.FeatureFlagDescription.builder()
                .name("name")
                .description("description")
                .build();

        featureFlags.update(id, description)
                .as(StepVerifier::create)
                .expectError(RecordNotFoundException.class)
                .verify();
    }

    @Test
    public void should_enable_success() {
        FeatureFlags featureFlags = new MockSuccessFeatureFlags();
        FeatureFlag.FeatureFlagId id = new FeatureFlag.FeatureFlagId("featureKey");

        featureFlags.enable(id)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    public void should_enable_return_error_when_id_not_exist() {
        FeatureFlags featureFlags = new MockNotFoundFeatureFlags();
        FeatureFlag.FeatureFlagId id = new FeatureFlag.FeatureFlagId("featureKey");

        featureFlags.enable(id)
                .as(StepVerifier::create)
                .expectError(RecordNotFoundException.class)
                .verify();
    }

    @Test
    public void should_disable_success() {
        FeatureFlags featureFlags = new MockSuccessFeatureFlags();
        FeatureFlag.FeatureFlagId id = new FeatureFlag.FeatureFlagId("featureKey");

        featureFlags.disable(id)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    public void should_disable_return_error_when_id_not_exist() {
        FeatureFlags featureFlags = new MockNotFoundFeatureFlags();
        FeatureFlag.FeatureFlagId id = new FeatureFlag.FeatureFlagId("featureKey");

        featureFlags.disable(id)
                .as(StepVerifier::create)
                .expectError(RecordNotFoundException.class)
                .verify();
    }


    static class MockSuccessFeatureFlags extends AbstractFeatureFlags {
        @Override
        public Mono<Void> doEnable(FeatureFlag featureFlag) {
            return Mono.empty();
        }

        @Override
        public Mono<Void> doUpdate(FeatureFlag featureFlag, FeatureFlag.FeatureFlagDescription description) {
            return Mono.empty();
        }

        @Override
        public Mono<Void> doDisable(FeatureFlag featureFlag) {
            return Mono.empty();
        }

        @Override
        public Mono<FeatureFlag> get(FeatureFlag.FeatureFlagId id) {
            return Mono.just(new FeatureFlag(id,
                    FeatureFlag.FeatureFlagDescription.builder().name("name").description("description").build()));
        }

        @Override
        public Flux<FeatureFlag> getAll(FilterParams params) {
            return null;
        }

        @Override
        public Flux<FeatureFlag> subCollection(FilterParams params, int from, int to) {
            return null;
        }

        @Override
        public Mono<Long> size(FilterParams params) {
            return null;
        }

        @Override
        public Flux<FeatureFlag> enabled(FilterParams params) {
            return null;
        }

        @Override
        public Mono<FeatureFlag> add(FeatureFlag featureFlag) {
            return null;
        }
    }

    static class MockNotFoundFeatureFlags extends AbstractFeatureFlags {
        @Override
        public Mono<Void> doEnable(FeatureFlag featureFlag) {
            return Mono.empty();
        }

        @Override
        public Mono<Void> doUpdate(FeatureFlag featureFlag, FeatureFlag.FeatureFlagDescription description) {
            return Mono.empty();
        }

        @Override
        public Mono<Void> doDisable(FeatureFlag featureFlag) {
            return Mono.empty();
        }

        @Override
        public Mono<FeatureFlag> get(FeatureFlag.FeatureFlagId id) {
            return Mono.empty();
        }

        @Override
        public Flux<FeatureFlag> getAll(FilterParams params) {
            return null;
        }

        @Override
        public Flux<FeatureFlag> subCollection(FilterParams params, int from, int to) {
            return null;
        }

        @Override
        public Mono<Long> size(FilterParams params) {
            return null;
        }

        @Override
        public Flux<FeatureFlag> enabled(FilterParams params) {
            return null;
        }

        @Override
        public Mono<FeatureFlag> add(FeatureFlag featureFlag) {
            return null;
        }
    }
}

