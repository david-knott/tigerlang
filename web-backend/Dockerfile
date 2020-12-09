FROM openjdk:11-jre-slim
#RUN addgroup -S spring && adduser -S spring -G spring
#USER spring:spring
ARG DEPENDENCY=target/
COPY ${DEPENDENCY}/web-backend-0.1.0.jar /app/
#COPY ${DEPENDENCY}/META-INF /app/META-INF
#COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-jar","/app/web-backend-0.1.0.jar"]
