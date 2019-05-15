
docker network create -d bridge --subnet=172.22.0.0/16 --gateway 172.22.0.1 smartstore_network
docker run --net smartstore_network --ip 172.22.0.4 -d --name smartstore_axon_server -p 8024:8024 -p 8124:8124 axoniq/axonserver:4.1.1
docker run --net smartstore_network --ip 172.22.0.5  -p 5432:5432 -e POSTGRES_PASSWORD=changeit -e POSTGRES_DB=master -d --name smartstore_postgres postgres:11.2
sleep 60
docker cp smartstore.sql smartstore_postgres:/smartstore.sql
docker exec -it  smartstore_postgres "/usr/bin/psql" "-d" "master" "-U" "postgres" "-f" "/smartstore.sql"
docker run --net smartstore_network --ip 172.22.0.6 -dit -p 7000:7000 --name smartstore_replenishment smartstore/julia-rep:1.0
docker run --net smartstore_network --ip 172.22.0.7 -dit -p 9456:9456 --name smartstore_backend smartstore/backend:1.0
docker run --net smartstore_network --ip 172.22.0.10 -dit -p 8080:8080 --name smartstore_ux smartstore/ux:1.0

