
FROM openjdk:17

WORKDIR /app

COPY build.gradle ./build.gradle
COPY .gradle ./.gradle
COPY gradle ./gradle
COPY gradlew ./gradlew

RUN ./gradlew build

COPY src ./src

CMD ["./gradlew", "run"]