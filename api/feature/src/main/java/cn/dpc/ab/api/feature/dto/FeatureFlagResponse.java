package cn.dpc.ab.api.feature.dto;

import cn.dpc.ab.domain.feature.FeatureFlag;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureFlagResponse {
    private String featureKey;

    private String name;

    private String description;

    private boolean enabled;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private FeatureFlag.FeatureTemplate template;

    public static FeatureFlagResponse from(FeatureFlag featureFlag) {
        return FeatureFlagResponse.builder()
                .featureKey(featureFlag.getId().featureKey())
                .name(featureFlag.getDescription().name())
                .description(featureFlag.getDescription().description())
                .enabled(featureFlag.getDescription().enabled())
                .createdAt(featureFlag.getDescription().createdAt())
                .updatedAt(featureFlag.getDescription().updatedAt())
                .template(featureFlag.getDescription().template())
                .build();
    }
}
