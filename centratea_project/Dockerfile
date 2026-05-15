FROM eclipse-temurin:17-jdk

WORKDIR /app
COPY src ./src

RUN javac -d out $(find src -name "*.java")

EXPOSE 12345
CMD ["java", "-cp", "out", "server.Server", "12345"]