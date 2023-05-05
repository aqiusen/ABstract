package cn.dpc.ab.persistent.feature;

import cn.dpc.ab.domain.feature.AbstractFeatureFlags;
import cn.dpc.ab.domain.feature.FeatureFlag;
import cn.dpc.ab.domain.feature.FeatureFlag.FeatureFlagDescription;
import cn.dpc.ab.persistent.feature.db.FeatureFlagDB;
import cn.dpc.ab.persistent.feature.db.FeatureFlagDBRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FeatureFlagsImpl extends AbstractFeatureFlags {
    private final FeatureFlagDBRepository repository;

    @Override
    public Mono<Void> doEnable(FeatureFlag featureFlag) {
        return updateToDB(featureFlag.getId().featureKey(), featureFlag.enable().getDescription());
    }

    @Override
    public Mono<Void> doUpdate(FeatureFlag featureFlag, FeatureFlagDescription description) {
        return updateToDB(featureFlag.getId().featureKey(), featureFlag.update(description).getDescription());
    }

    @Override
    public Mono<Void> doDisable(FeatureFlag featureFlag) {
        return updateToDB(featureFlag.getId().featureKey(), featureFlag.disable().getDescription());
    }

    @Override
    public Mono<FeatureFlag> get(FeatureFlag.FeatureFlagId id) {
        return repository.findById(FeatureFlagDB.idFromFeatureKey(id.featureKey()))
                .map(FeatureFlagDB::to);
    }

    @Override
    public Flux<FeatureFlag> getAll(FilterParams params) {
        return getAllByFilter(params)
                .map(FeatureFlagDB::to);
    }

    private Flux<FeatureFlagDB> getAllByFilter(FilterParams params) {
        return repository.findAll()
                .filter(db -> (params.key() == null || db.getFeatureKey().contains(params.key()))
                        && (params.enabled() == null || db.getDescription().enabled() == params.enabled())
                );
    }

    @Override
    public Flux<FeatureFlag> subCollection(FilterParams params, int from, int to) {
        return this.getAll(params).skip(from).take(to - from);
    }

    @Override
    public Mono<Long> size(FilterParams params) {
        return getAllByFilter(params).count();
    }

    @Override
    public Flux<FeatureFlag> enabled(FilterParams params) {
        return getAll(new FilterParams(params.key(), true));
    }

    @Override
    public Mono<FeatureFlag> add(FeatureFlag featureFlag) {
        return repository.save(FeatureFlagDB.from(featureFlag)).map(FeatureFlagDB::to);
    }

    private Mono<Void> updateToDB(String featureKey, FeatureFlagDescription description) {
         return repository.findById(FeatureFlagDB.idFromFeatureKey(featureKey))
                .flatMap(db -> {
                    db.setDescription(description);
                    return repository.save(db);
                }).then();
    }
}
