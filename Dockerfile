FROM eclipse-temurin:21-jdk AS builder
WORKDIR workspace
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} franchise-app.jar
RUN java -Djarmode=layertools -jar franchise-app.jar extract

FROM eclipse-temurin:21-jre
RUN useradd spring
USER spring
WORKDIR workspace
COPY --from=builder workspace/dependencies/ ./
COPY --from=builder workspace/spring-boot-loader/ ./
COPY --from=builder workspace/snapshot-dependencies/ ./
COPY --from=builder workspace/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
