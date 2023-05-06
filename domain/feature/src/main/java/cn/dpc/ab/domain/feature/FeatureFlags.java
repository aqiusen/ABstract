package cn.dpc.ab.domain.feature;

import lombok.Builder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * FeatureFlags provides an interface for managing feature flags.
 */
public interface FeatureFlags {


    /**
     * FilterParams provides a set of parameters to filter feature flags.
     */
    @Builder
    record FilterParams(String key, Boolean enabled) {}

    /**
     * Get a feature flag.
     *
     * @param id the id of the feature flag to get
     * @return a Mono that completes when the feature flag has been retrieved
     */
    Mono<FeatureFlag> get(FeatureFlag.FeatureFlagId id);

    /**
     * Get all feature flags.
     *
     * @param params the filter params
     * @return a Flux that emits all feature flags
     */
    Flux<FeatureFlag> getAll(FilterParams params);

    /**
     * Get a sub-collection of feature flags.
     *
     * @param params the filter params
     * @param from   the start index of the sub-collection
     * @param to     the end index of the sub-collection
     * @return a Flux that emits a sub-collection of feature flags
     */
    Flux<FeatureFlag> subCollection(FilterParams params, int from, int to);

    /**
     * Get the size of a sub-collection of feature flags.
     *
     * @param params the filter params
     * @return a Mono that emits the size of a sub-collection of feature flags
     */
    Mono<Long> size(FilterParams params);

    /**
     * Get all enabled feature flags.
     *
     * @param params the filter params
     * @return a Flux that emits all enabled feature flags
     */
    Flux<FeatureFlag> enabled(FilterParams params);


    /**
     * add a feature flag.
     *
     * @param featureFlag the feature flag to add
     * @return a Mono that completes when the feature flag has been added
     */
    Mono<FeatureFlag> add(FeatureFlag featureFlag);

    /**
     * Update a feature flag.
     *
     * @param id          the id of the feature flag to update
     * @param description the new description of the feature flag
     * @return a Mono that completes when the feature flag has been updated
     */
    Mono<Void> update(FeatureFlag.FeatureFlagId id, FeatureFlag.FeatureFlagDescription description);

    /**
     * Enable a feature flag.
     *
     * @param id the id of the feature flag to enable
     * @return a Mono that completes when the feature flag has been enabled
     */
    Mono<Void> enable(FeatureFlag.FeatureFlagId id);

    /**
     * Disable a feature flag.
     *
     * @param id the id of the feature flag to disable
     * @return a Mono that completes when the feature flag has been disabled
     */
    Mono<Void> disable(FeatureFlag.FeatureFlagId id);
}
