package cn.dpc.ab.api.feature;

import cn.dpc.ab.api.TestConfiguration;
import cn.dpc.ab.api.feature.dto.FeatureFlagRequest;
import cn.dpc.ab.api.feature.dto.FeatureFlagResponse;
import cn.dpc.ab.domain.feature.FeatureFlag;
import cn.dpc.ab.domain.feature.FeatureFlags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(value = FeatureFlagApi.class, properties = "spring.main.lazy-initialization=true")
@ContextConfiguration(classes = TestConfiguration.class)
class FeatureFlagApiTest {

    @Autowired
    WebTestClient webClient;

    @MockBean
    FeatureFlags featureFlags;

    @Test
    public void should_getAll_success_when_no_records() {
        when(featureFlags.getAll(Mockito.any())).thenReturn(Flux.empty());

        webClient.get()
                .uri("/feature-flags")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(FeatureFlagResponse.class)
                .hasSize(0);
    }

    @Test
    public void should_getAll_success_when_has_records() {
        when(featureFlags.getAll(Mockito.any())).thenReturn(Flux.just(createFeatureFlag("featureKey1"),
                createFeatureFlag("featureKey2")));

        webClient.get()
                .uri("/feature-flags")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(FeatureFlagResponse.class)
                .hasSize(2)
                .value(response -> {
                    assertEquals(response.get(0).getFeatureKey(), "featureKey1");
                    assertEquals(response.get(1).getFeatureKey(), "featureKey2");
                });
    }

    @Test
    public void should_add_feature_flag_success() {
        when(featureFlags.add(Mockito.any())).thenReturn(Mono.just(createFeatureFlag("featureKey1")));

        webClient.post()
                .uri("/feature-flags")
                .body(Mono.just(createFeatureFlagRequest("featureKey1")), FeatureFlagRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(FeatureFlagResponse.class)
                .value(response -> {
                    assertEquals(response.getFeatureKey(), "featureKey1");
                });
    }

    @Test
    public void should_add_feature_flag_failed_when_feature_key_is_duplicated() {
        when(featureFlags.add(Mockito.any())).thenReturn(Mono.error(new DuplicateKeyException("feature key is duplicated")));

        webClient.post()
                .uri("/feature-flags")
                .body(Mono.just(createFeatureFlagRequest("featureKey1")), FeatureFlagRequest.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
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

    public static FeatureFlagRequest createFeatureFlagRequest(String featureKey) {
        return FeatureFlagRequest.builder()
                .name("name")
                .featureKey(featureKey)
                .description("description")
                .enabled(false)
                .template(null)
                .build();
    }

}