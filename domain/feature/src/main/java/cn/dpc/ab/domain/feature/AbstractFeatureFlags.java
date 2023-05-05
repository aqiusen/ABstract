package cn.dpc.ab.domain.feature;

import cn.dpc.ab.domain.exception.RecordNotFoundException;
import reactor.core.publisher.Mono;

public abstract class AbstractFeatureFlags implements FeatureFlags {
    public abstract Mono<Void> doEnable(FeatureFlag featureFlag);


    public Mono<Void> enable(FeatureFlag.FeatureFlagId id) {
        return this.get(id)
                .switchIfEmpty(Mono.error(new RecordNotFoundException(id.toString())))
                .flatMap(featureFlag -> doEnable(featureFlag));
    }

    public abstract Mono<Void> doUpdate(FeatureFlag featureFlag, FeatureFlag.FeatureFlagDescription description);

    public Mono<Void> update(FeatureFlag.FeatureFlagId id, FeatureFlag.FeatureFlagDescription description) {
        return this.get(id)
                .switchIfEmpty(Mono.error(new RecordNotFoundException(id.toString())))
                .flatMap(featureFlag -> doUpdate(featureFlag, description));
    }

    public abstract Mono<Void> doDisable(FeatureFlag featureFlag);


    public Mono<Void> disable(FeatureFlag.FeatureFlagId id) {
        return this.get(id)
                .switchIfEmpty(Mono.error(new RecordNotFoundException(id.toString())))
                .flatMap(featureFlag -> doDisable(featureFlag));
    }
}
