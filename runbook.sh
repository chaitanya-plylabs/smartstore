docker run --name smartstore-postgres -p 5432:5432 -e POSTGRES_PASSWORD=changeit -e POSTGRES_DB=master -d postgres:11.2
docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' smartstore-postgres
docker run -d --name smartstore-axon-server -p 8024:8024 -p 8124:8124 axoniq/axonserver:4.1.1