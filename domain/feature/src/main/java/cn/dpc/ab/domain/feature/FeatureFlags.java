package cn.dpc.ab.domain.feature;

import lombok.Builder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FeatureFlags {

    @Builder
    record FilterParams(String key, Boolean enabled) {}

    Mono<FeatureFlag> get(FeatureFlag.FeatureFlagId id);

    Flux<FeatureFlag> getAll(FilterParams params);

    Flux<FeatureFlag> subCollection(FilterParams params, int from, int to);

    Mono<Long> size(FilterParams params);

    Flux<FeatureFlag> enabled(FilterParams params);

    Mono<FeatureFlag> add(FeatureFlag featureFlag);

    Mono<Void> update(FeatureFlag.FeatureFlagId id, FeatureFlag.FeatureFlagDescription description);

    Mono<Void> enable(FeatureFlag.FeatureFlagId id);

    Mono<Void> disable(FeatureFlag.FeatureFlagId id);
}
