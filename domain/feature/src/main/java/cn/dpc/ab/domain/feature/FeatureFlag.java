package cn.dpc.ab.domain.feature;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class FeatureFlag {
    private FeatureFlagId id;
    private FeatureFlagDescription description;

    public FeatureFlag enable() {
        this.description = new FeatureFlagDescription(description.name(),
                description.description(),
                description.template(),
                true,
                description.createdAt(),
                LocalDateTime.now());

        return this;
    }

    public FeatureFlag update(FeatureFlagDescription description) {
        this.description = new FeatureFlagDescription(description.name(),
                description.description(),
                description.template(),
                description.enabled(),
                description.createdAt(),
                LocalDateTime.now());

        return this;
    }

    public FeatureFlag disable() {
        this.description = new FeatureFlagDescription(description.name(),
                description.description(),
                description.template(),
                false,
                description.createdAt(),
                LocalDateTime.now());

        return this;
    }

    @Builder
    public record FeatureFlagId(String featureKey) {
    }

    @Builder
    public record FeatureFlagDescription(String name, String description, FeatureTemplate template, boolean enabled,
                                         LocalDateTime createdAt, LocalDateTime updatedAt) {
    }

    public record FeatureTemplate(DataType dataType, String key, String name, String description,
                                  List<FeatureTemplate> items) {
        public enum DataType {
            BOOLEAN, STRING, NUMBER, OBJECT, ARRAY
        }
    }
}
