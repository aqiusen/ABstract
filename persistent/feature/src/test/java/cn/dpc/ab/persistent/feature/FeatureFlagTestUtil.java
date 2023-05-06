package cn.dpc.ab.persistent.feature;

import cn.dpc.ab.domain.feature.FeatureFlag;
import cn.dpc.ab.persistent.feature.db.FeatureFlagDb;

import java.time.LocalDateTime;

/**
 * Feature flag test util.
 */
public class FeatureFlagTestUtil {

    /**
     * Create a feature flag db.
     *
     * @param featureKey the feature key
     * @return the feature flag db
     */
    public static FeatureFlagDb createFeatureFlagDb(String featureKey) {
        return createFeatureFlagDb(featureKey, false);
    }

    /**
     * Create a feature flag db.
     *
     * @param featureKey the feature key
     * @param enabled    the enabled
     * @return the feature flag db
     */
    public static FeatureFlagDb createFeatureFlagDb(String featureKey, boolean enabled) {
        FeatureFlagDb featureFlagDb = new FeatureFlagDb();
        featureFlagDb.setFeatureKey(featureKey);
        featureFlagDb.setId(FeatureFlagDb.idFromFeatureKey(featureKey));
        featureFlagDb.setDescription(FeatureFlag.FeatureFlagDescription.builder()
                .description("description")
                .name("name")
                .enabled(enabled)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .template(null)
                .build());
        return featureFlagDb;
    }

    /**
     * Create a feature flag.
     *
     * @param featureKey the feature key
     * @return the feature flag
     */
    public static FeatureFlag createFeatureFlag(String featureKey) {
        return new FeatureFlag(new FeatureFlag.FeatureFlagId(featureKey),
                FeatureFlag.FeatureFlagDescription.builder()
                        .description("description")
                        .name("name")
                        .enabled(false)
                        .template(null)
                        .build());
    }
}
