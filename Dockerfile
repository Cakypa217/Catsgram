FROM amazoncorretto:21 AS builder
WORKDIR /app
COPY target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM amazoncorretto:21
ENV CATSGRAM_IMAGE_DIRECTORY=/app/images
COPY --from=builder /app/app.jar ./app.jar
COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./
ENTRYPOINT ["java", "-jar", "app.jar"]