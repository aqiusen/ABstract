package cn.dpc.ab.persistent.feature.db;

import com.couchbase.client.java.query.QueryScanConsistency;
import org.springframework.data.couchbase.repository.ScanConsistency;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 * FeatureFlagDBRepository provides a skeleton implementation of the FeatureFlagDBRepository interface.
 */
@ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
public interface FeatureFlagDbRepository extends ReactiveCrudRepository<FeatureFlagDb, String> {

    Flux<FeatureFlagDb> findAll();
}
