package cn.dpc.ab.domain.feature;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * FeatureFlag represents a feature flag.
 */
@AllArgsConstructor
@Getter
public class FeatureFlag {
    private FeatureFlagId id;
    private FeatureFlagDescription description;

    /**
     * Enable a feature flag.
     *
     * @return a Mono that completes when the feature flag has been enabled
     */
    public FeatureFlag enable() {
        this.description = new FeatureFlagDescription(description.name(),
                description.description(),
                description.template(),
                true,
                description.createdAt(),
                LocalDateTime.now());

        return this;
    }

    /**
     * Update a feature flag.
     *
     * @param description the new description of the feature flag
     * @return a Mono that completes when the feature flag has been updated
     */
    public FeatureFlag update(FeatureFlagDescription description) {
        this.description = new FeatureFlagDescription(description.name(),
                description.description(),
                description.template(),
                description.enabled(),
                description.createdAt(),
                LocalDateTime.now());

        return this;
    }

    /**
     * Disable a feature flag.
     *
     * @return a Mono that completes when the feature flag has been disabled
     */
    public FeatureFlag disable() {
        this.description = new FeatureFlagDescription(description.name(),
                description.description(),
                description.template(),
                false,
                description.createdAt(),
                LocalDateTime.now());

        return this;
    }


    /**
     * FeatureFlagId represents the id of a feature flag.
     */
    @Builder
    public record FeatureFlagId(String featureKey) {
    }

    /**
     * FeatureFlagDescription represents the description of a feature flag.
     */
    @Builder
    public record FeatureFlagDescription(String name, String description, FeatureTemplate template, boolean enabled,
                                         LocalDateTime createdAt, LocalDateTime updatedAt) {
    }

    /**
     * FeatureConfigTemplate represents the template of a feature flag.
     */
    public record FeatureTemplate(DataType dataType, String key, String name, String description,
                                  List<FeatureTemplate> items) {
        /**
         * DataType represents the data type of a feature flag.
         */
        public enum DataType {
            BOOLEAN, STRING, NUMBER, OBJECT, ARRAY
        }
    }
}
