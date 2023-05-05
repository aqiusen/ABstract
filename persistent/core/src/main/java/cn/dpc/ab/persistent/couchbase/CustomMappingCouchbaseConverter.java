package cn.dpc.ab.persistent.couchbase;

import org.springframework.data.couchbase.core.convert.MappingCouchbaseConverter;
import org.springframework.data.couchbase.core.mapping.CouchbaseDocument;
import org.springframework.data.couchbase.core.mapping.CouchbasePersistentEntity;
import org.springframework.data.couchbase.core.mapping.CouchbasePersistentProperty;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.util.TypeInformation;

public class CustomMappingCouchbaseConverter extends MappingCouchbaseConverter {

    public CustomMappingCouchbaseConverter(MappingContext<? extends CouchbasePersistentEntity<?>,
            CouchbasePersistentProperty> mappingContext) {
        super(mappingContext);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <R> R read(final TypeInformation<R> type, final CouchbaseDocument source, final Object parent) {
        if (type == null) {
            return (R) source.export();
        }
        if (Object.class == typeMapper.readType(source, type).getType()) {
            return (R) source.export();
        } else {
            return super.read(type, source, parent);
        }
    }
}
