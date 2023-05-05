#!/bin/bash

# Monitor mode (used to attach into couchbase entrypoint)
set -m
# Send it to background
/entrypoint.sh couchbase-server &

# Check if couchbase server is up
check_db() {
  curl --silent http://127.0.0.1:8091/pools > /dev/null
  echo $?
}

# Variable used in echo
i=1
# Echo with
numbered_echo() {
  echo "[$i] $@"
  i=`expr $i + 1`
}

# Parse JSON and get nodes from the cluster
read_nodes() {
  cmd="import sys,json;"
  cmd="${cmd} print(','.join([node['otpNode']"
  cmd="${cmd} for node in json.load(sys.stdin)['nodes']"
  cmd="${cmd} ]))"
  python -c "${cmd}"
}

# Wait until it's ready
until [[ $(check_db) = 0 ]]; do
  >&2 numbered_echo "Waiting for Couchbase Server to be available"
  sleep 1
done

echo "# Couchbase Server Online"
echo "# Starting setup process"

HOSTNAME=`hostname -f`

# Reset steps
i=1

numbered_echo "Setup Services"
curl -u "${ADMINNAME}:${ADMINPASSWORD}" -v -X POST http://localhost:8091/node/controller/setupServices
  -d services=[kv | index | n1ql | fts | cbas | eventing]

# Setup Administrator ADMINNAME and ADMINPASSWORD
curl -u Administrator:ADMINPASSWORD -v -X POST http://[localhost]:8091/settings/web
  -d ADMINPASSWORD=[ADMINPASSWORD]
  -d ADMINNAME=[admin-name]
  -d port=8091



# Configure
numbered_echo "Initialize the node"
curl --silent -u "${ADMINNAME}:${ADMINPASSWORD}" -X POST "http://${HOSTNAME}:8091/nodes/self/controller/settings" \
  -d path="/opt/couchbase/var/lib/couchbase/data" \
  -d index_path="/opt/couchbase/var/lib/couchbase/data"

numbered_echo "Setting hostname"
curl --silent -u "${ADMINNAME}:${ADMINPASSWORD}" -X POST "http://${HOSTNAME}:8091/node/controller/rename" \
  -d hostname=${HOSTNAME}


numbered_echo "Setting up memory"
curl --silent -u "${ADMINNAME}:${ADMINPASSWORD}" -X POST "http://${HOSTNAME}:8091/pools/default" \
  -d memoryQuota=${MEMORY_QUOTA} \
  -d indexMemoryQuota=${INDEX_MEMORY_QUOTA} \
  -d ftsMemoryQuota=${FTS_MEMORY_QUOTA}

numbered_echo "Setting up services"
curl --silent -u "${ADMINNAME}:${ADMINPASSWORD}" -X POST "http://${HOSTNAME}:8091/node/controller/setupServices" \
  -d services="${SERVICES}"

numbered_echo "Setting up user credentials"
curl --silent -u "${ADMINNAME}:${ADMINPASSWORD}" -X POST "http://${HOSTNAME}:8091/settings/web" \
  -d port=8091 \
  -d username=${ADMINNAME} \
  -d password=${ADMINPASSWORD} > /dev/null

numbered_echo "Setup Bucket"
curl --silent -u "${ADMINNAME}:${ADMINPASSWORD}" -X POST "http://${HOSTNAME}:8091/pools/default/buckets" \
  -d flushEnabled=1 \
  -d replicaNumber=0 \
  -d evictionPolicy=valueOnly \
  -d ramQuotaMB=256 \
  -d bucketType=couchbase \
  -d name="${BUCKETNAME}" \

numbered_echo "Set User"
curl  --silent -u "${ADMINNAME}:${ADMINPASSWORD}" -X PUT --data "name=${USERNAME}&roles=admin&password=${USERPASSWORD}" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  "http://${HOSTNAME}:8091/settings/rbac/users/local/${USERNAME}"

numbered_echo "Set Index storageMode"
curl --silent -u "${ADMINNAME}:${ADMINPASSWORD}" -X POST "http://${HOSTNAME}:8091/settings/indexes" \
-d storageMode=forestdb

printf ""
sleep 5
numbered_echo "Create Primary Index"
curl -v -u "${ADMINNAME}:${ADMINPASSWORD}" -X POST  "http://${HOSTNAME}:8093/query/service" \
-d "statement=CREATE PRIMARY INDEX ON \`${BUCKETNAME}\`"

# Attach to couchbase entrypoint
numbered_echo "Attaching to couchbase-server entrypoint"
fg 1