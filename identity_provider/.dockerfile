FROM maven as builder

WORKDIR /app

COPY ./pom.xml .
RUN mvn dependency:go-offline

COPY ./src ./src
RUN mvn package -DskipTests


FROM openjdk:17

WORKDIR /app

COPY --from=builder /app/target/demo-0.0.1-SNAPSHOT.jar .


EXPOSE 9000

ENTRYPOINT ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]