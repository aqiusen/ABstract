package cn.dpc.ab.domain.feature;

import cn.dpc.ab.domain.exception.RecordNotFoundException;
import reactor.core.publisher.Mono;

/**
 * AbstractFeatureFlags provides a skeleton implementation of the FeatureFlags interface.
 */
public abstract class AbstractFeatureFlags implements FeatureFlags {

    /**
     * Enable a feature flag.
     *
     * @param featureFlag the feature flag to enable
     * @return a Mono that completes when the feature flag has been enabled
     */
    protected abstract Mono<Void> doEnable(FeatureFlag featureFlag);


    /**
     * Enable a feature flag.
     *
     * @param id the id of the feature flag to enable
     * @return a Mono that completes when the feature flag has been enabled
     */
    public Mono<Void> enable(FeatureFlag.FeatureFlagId id) {
        return this.get(id)
                .switchIfEmpty(Mono.error(new RecordNotFoundException(id.toString())))
                .flatMap(featureFlag -> doEnable(featureFlag));
    }

    /**
     * Update a feature flag.
     *
     * @param featureFlag the feature flag to update
     * @param description the new description of the feature flag
     * @return a Mono that completes when the feature flag has been updated
     */
    protected abstract Mono<Void> doUpdate(FeatureFlag featureFlag, FeatureFlag.FeatureFlagDescription description);

    /**
     * Update a feature flag.
     *
     * @param id          the id of the feature flag to update
     * @param description the new description of the feature flag
     * @return a Mono that completes when the feature flag has been updated
     */
    public Mono<Void> update(FeatureFlag.FeatureFlagId id, FeatureFlag.FeatureFlagDescription description) {
        return this.get(id)
                .switchIfEmpty(Mono.error(new RecordNotFoundException(id.toString())))
                .flatMap(featureFlag -> doUpdate(featureFlag, description));
    }

    /**
     * Disable a feature flag.
     *
     * @param featureFlag the feature flag to disable
     * @return a Mono that completes when the feature flag has been disabled
     */
    protected abstract Mono<Void> doDisable(FeatureFlag featureFlag);


    /**
     * Disable a feature flag.
     *
     * @param id the id of the feature flag to disable
     * @return a Mono that completes when the feature flag has been disabled
     */
    public Mono<Void> disable(FeatureFlag.FeatureFlagId id) {
        return this.get(id)
                .switchIfEmpty(Mono.error(new RecordNotFoundException(id.toString())))
                .flatMap(featureFlag -> doDisable(featureFlag));
    }
}
