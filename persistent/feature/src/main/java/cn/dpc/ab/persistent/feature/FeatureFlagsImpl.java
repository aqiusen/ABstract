package cn.dpc.ab.persistent.feature;

import cn.dpc.ab.domain.feature.AbstractFeatureFlags;
import cn.dpc.ab.domain.feature.FeatureFlag;
import cn.dpc.ab.domain.feature.FeatureFlag.FeatureFlagDescription;
import cn.dpc.ab.persistent.feature.db.FeatureFlagDb;
import cn.dpc.ab.persistent.feature.db.FeatureFlagDbRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * FeatureFlagsImpl provides a skeleton implementation of the FeatureFlags interface.
 */
@Component
@RequiredArgsConstructor
public class FeatureFlagsImpl extends AbstractFeatureFlags {
    private final FeatureFlagDbRepository repository;

    /**
     * enableToDb enables a feature flag in the database.
     *
     * @see cn.dpc.ab.domain.feature.AbstractFeatureFlags#doEnable(cn.dpc.ab.domain.feature.FeatureFlag)
     */
    @Override
    public Mono<Void> doEnable(FeatureFlag featureFlag) {
        return updateToDb(featureFlag.getId().featureKey(), featureFlag.enable().getDescription());
    }

    /**
     * doUpdate updates a feature flag in the database.
     *
     * @see cn.dpc.ab.domain.feature.AbstractFeatureFlags#doUpdate(cn.dpc.ab.domain.feature.FeatureFlag,
     * cn.dpc.ab.domain.feature.FeatureFlag.FeatureFlagDescription)
     */
    @Override
    protected Mono<Void> doUpdate(FeatureFlag featureFlag, FeatureFlagDescription description) {
        return updateToDb(featureFlag.getId().featureKey(), featureFlag.update(description).getDescription());
    }

    /**
     * doDisable disables a feature flag in the database.
     *
     * @see cn.dpc.ab.domain.feature.AbstractFeatureFlags#doDisable(cn.dpc.ab.domain.feature.FeatureFlag)
     */
    @Override
    public Mono<Void> doDisable(FeatureFlag featureFlag) {
        return updateToDb(featureFlag.getId().featureKey(), featureFlag.disable().getDescription());
    }

    /**
     * get retrieves a feature flag from the database.
     *
     * @see cn.dpc.ab.domain.feature.FeatureFlags#get(cn.dpc.ab.domain.feature.FeatureFlag.FeatureFlagId)
     */
    @Override
    public Mono<FeatureFlag> get(FeatureFlag.FeatureFlagId id) {
        return repository.findById(FeatureFlagDb.idFromFeatureKey(id.featureKey()))
                .map(FeatureFlagDb::to);
    }

    /**
     * getAll retrieves all feature flags from the database.
     *
     * @see cn.dpc.ab.domain.feature.FeatureFlags#getAll(cn.dpc.ab.domain.feature.FeatureFlags.FilterParams)
     */
    @Override
    public Flux<FeatureFlag> getAll(FilterParams params) {
        return getAllByFilter(params)
                .map(FeatureFlagDb::to);
    }

    /**
     * getAllByFilter retrieves all feature flags from the database that match the
     * given filter.
     *
     * @param params the filter parameters
     * @return a Flux of FeatureFlagDb
     */
    private Flux<FeatureFlagDb> getAllByFilter(FilterParams params) {
        return repository.findAll()
                .filter(db -> (params.key() == null || db.getFeatureKey().contains(params.key()))
                        && (params.enabled() == null || db.getDescription().enabled() == params.enabled())
                );
    }

    /**
     * subCollection retrieves a sub-collection of feature flags from the database
     * that match the given filter.
     *
     * @see cn.dpc.ab.domain.feature.FeatureFlags#subCollection(cn.dpc.ab.domain.feature.FeatureFlags.FilterParams,
     * int, int)
     */
    @Override
    public Flux<FeatureFlag> subCollection(FilterParams params, int from, int to) {
        return this.getAll(params).skip(from).take(to - from);
    }

    /**
     * size retrieves the number of feature flags from the database that match the
     * given filter.
     *
     * @see cn.dpc.ab.domain.feature.FeatureFlags#size(cn.dpc.ab.domain.feature.FeatureFlags.FilterParams)
     */
    @Override
    public Mono<Long> size(FilterParams params) {
        return getAllByFilter(params).count();
    }

    /**
     * enabled retrieves all enabled feature flags from the database.
     *
     * @see cn.dpc.ab.domain.feature.FeatureFlags#enabled(cn.dpc.ab.domain.feature.FeatureFlags.FilterParams)
     */
    @Override
    public Flux<FeatureFlag> enabled(FilterParams params) {
        return getAll(new FilterParams(params.key(), true));
    }

    /**
     * disabled retrieves all disabled feature flags from the database.
     *
     * @see cn.dpc.ab.domain.feature.FeatureFlags#add(cn.dpc.ab.domain.feature.FeatureFlag)
     */
    @Override
    public Mono<FeatureFlag> add(FeatureFlag featureFlag) {
        return repository.save(FeatureFlagDb.from(featureFlag)).map(FeatureFlagDb::to);
    }

    /**
     * updateToDb updates a feature flag in the database.
     *
     * @param featureKey  the feature key
     * @param description the feature flag description
     * @return a Mono of Void
     */
    private Mono<Void> updateToDb(String featureKey, FeatureFlagDescription description) {
        return repository.findById(FeatureFlagDb.idFromFeatureKey(featureKey))
                .flatMap(db -> {
                    db.setDescription(description);
                    return repository.save(db);
                }).then();
    }
}
