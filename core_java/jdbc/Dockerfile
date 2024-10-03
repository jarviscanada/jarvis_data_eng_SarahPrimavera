FROM openjdk:8-alpine
WORKDIR /app
COPY pom.xml /app/
COPY src /app/src
ENTRYPOINT ["java", "-cp",  "target/classes:target/dependency/*", "ca.jrvs.apps.jdbc.StockQuoteApp"]