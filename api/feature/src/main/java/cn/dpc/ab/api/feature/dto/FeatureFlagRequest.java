package cn.dpc.ab.api.feature.dto;

import cn.dpc.ab.domain.feature.FeatureFlag;
import lombok.*;

/**
 * FeatureFlagRequest represents a feature flag in the API.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeatureFlagRequest {
    private String featureKey;

    private String name;

    private String description;

    private boolean enabled;

    private FeatureFlag.FeatureTemplate template;


    /**
     * Creates a FeatureFlag from a FeatureFlagRequest.
     *
     * @return the FeatureFlag
     */
    public FeatureFlag toFeatureFlag() {
        return new FeatureFlag(
                new FeatureFlag.FeatureFlagId(featureKey),
                FeatureFlag.FeatureFlagDescription.builder()
                        .name(name)
                        .description(description)
                        .enabled(enabled)
                        .template(template)
                        .build());
    }
}
