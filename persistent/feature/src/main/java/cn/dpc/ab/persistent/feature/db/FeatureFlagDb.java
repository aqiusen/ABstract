package cn.dpc.ab.persistent.feature.db;

import cn.dpc.ab.domain.feature.FeatureFlag;
import cn.dpc.ab.domain.feature.FeatureFlag.FeatureFlagDescription;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.IdAttribute;
import org.springframework.data.couchbase.core.mapping.id.IdPrefix;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.data.couchbase.core.mapping.id.GenerationStrategy.USE_ATTRIBUTES;

/**
 * FeatureFlagDb represents a feature flag in the database.
 */
@Document
@Data
public class FeatureFlagDb {

    @Version
    private long version;

    @IdPrefix
    private static final String PREFIX = "feature_flag";

    @Id
    @GeneratedValue(strategy = USE_ATTRIBUTES)
    private String id;

    @IdAttribute
    private String featureKey;

    @Field
    private FeatureFlagDescription description;


    public static String idFromFeatureKey(String featureKey) {
        return PREFIX + "." + featureKey;
    }

    /**
     * Creates a FeatureFlagDb from a FeatureFlag.
     *
     * @param featureFlag the FeatureFlag
     * @return the FeatureFlagDb
     */
    public static FeatureFlagDb from(FeatureFlag featureFlag) {
        FeatureFlagDb db = new FeatureFlagDb();
        db.setId(idFromFeatureKey(featureFlag.getId().featureKey()));
        db.setFeatureKey(featureFlag.getId().featureKey());
        db.setDescription(from(featureFlag.getDescription()));
        return db;
    }

    private static FeatureFlagDescription from(FeatureFlagDescription description) {
        return FeatureFlagDescription.builder()
                .name(description.name())
                .description(description.description())
                .enabled(description.enabled())
                .createdAt(Optional.ofNullable(description.createdAt()).orElse(LocalDateTime.now()))
                .updatedAt(Optional.ofNullable(description.updatedAt()).orElse(LocalDateTime.now()))
                .template(description.template())
                .build();
    }

    public FeatureFlag to() {
        return new FeatureFlag(FeatureFlag.FeatureFlagId.builder().featureKey(this.getFeatureKey()).build(),
                this.getDescription());
    }
}
