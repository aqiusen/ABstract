FROM couchbase/server:community-7.1.1

ENV MEMORY_QUOTA 256
ENV INDEX_MEMORY_QUOTA 256
ENV FTS_MEMORY_QUOTA 256

ENV SERVICES "kv,n1ql,index,fts"

ENV ADMINNAME "Administrator"
ENV ADMINPASSWORD "password"
ENV USERNAME "bucket1"
ENV USERPASSWORD "bucket1pass"
ENV BUCKETNAME "bucket1"

ENV CLUSTER_HOST ""
ENV CLUSTER_REBALANCE ""

COPY couchbase-entrypoint.sh /config-entrypoint.sh

RUN chmod +x /config-entrypoint.sh

ENTRYPOINT ["/config-entrypoint.sh"]