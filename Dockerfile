FROM ghcr.io/graalvm/graalvm-ce:21.3.0

WORKDIR /app

COPY build/libs/kameleoon-test-task-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 5000

ENTRYPOINT ["java", "-jar", "app.jar"]