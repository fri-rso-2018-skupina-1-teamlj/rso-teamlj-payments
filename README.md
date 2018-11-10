# RSO: Payments microservice

## Prerequisites

```bash
docker run -d --name pg-payments -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=payment -p 5435:5432 postgres:latest
```

Local run (warning: debugger needs to be attached):
```
java -agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=y -jar api/target/payments-api-1.0.0-SNAPSHOT.jar
```

```
docker build -t payments:1.0 .
docker run -p 8083:8083 payments:1.0
to change network host: docker run -p 8083:8083 --net=host payments:1.0
```
