FROM openjdk:8-jre-slim

RUN mkdir /app

WORKDIR /app

ADD ./api/target/payments-api-1.0.0-SNAPSHOT.jar /app

EXPOSE 8083

CMD java -jar payments-api-1.0.0-SNAPSHOT.jar
