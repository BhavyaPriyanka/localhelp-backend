# ---------- Build Stage ----------
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /build

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

# ---------- Runtime Stage ----------
FROM gcr.io/distroless/java21-debian12:nonroot


WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar


EXPOSE 8080

ENTRYPOINT [ "java","-jar","app.jar" ]