package cn.dpc.ab.persistent.feature;

import cn.dpc.ab.domain.feature.FeatureFlag;
import cn.dpc.ab.persistent.feature.db.FeatureFlagDB;

import java.time.LocalDateTime;

public class FeatureFlagTestUtil {
    public static FeatureFlagDB createFeatureFlagDB(String featureKey) {
        return createFeatureFlagDB(featureKey, false);
    }

    public static FeatureFlagDB createFeatureFlagDB(String featureKey, boolean enabled) {
        FeatureFlagDB featureFlagDB = new FeatureFlagDB();
        featureFlagDB.setFeatureKey(featureKey);
        featureFlagDB.setId(FeatureFlagDB.idFromFeatureKey(featureKey));
        featureFlagDB.setDescription(FeatureFlag.FeatureFlagDescription.builder()
                .description("description")
                .name("name")
                .enabled(enabled)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .template(null)
                .build());
        return featureFlagDB;
    }

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
