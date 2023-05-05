package cn.dpc.ab.persistent.feature.db;

import com.couchbase.client.java.query.QueryScanConsistency;
import org.springframework.data.couchbase.repository.ScanConsistency;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

@ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
public interface FeatureFlagDBRepository extends ReactiveCrudRepository<FeatureFlagDB, String> {

    Flux<FeatureFlagDB> findAll();
}
