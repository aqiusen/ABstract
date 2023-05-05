package cn.dpc.ab.api.feature;

import cn.dpc.ab.api.feature.dto.FeatureFlagRequest;
import cn.dpc.ab.api.feature.dto.FeatureFlagResponse;
import cn.dpc.ab.domain.feature.FeatureFlag;
import cn.dpc.ab.domain.feature.FeatureFlags;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/feature-flags")
@RequiredArgsConstructor
public class FeatureFlagApi {
    private final FeatureFlags featureFlags;


    @GetMapping
    Flux<FeatureFlagResponse> getAll(FeatureFlags.FilterParams params) {
        return featureFlags.getAll(params)
                .map(FeatureFlagResponse::from);
    }

    @PostMapping
    Mono<FeatureFlagResponse> add(@RequestBody FeatureFlagRequest request) {
        return featureFlags.add(request.toFeatureFlag())
                .map(FeatureFlagResponse::from);
    }
}
