FROM maven as build
WORKDIR /usr/src/app
COPY . .
#RUN mvn -B -f /tmp/pom.xml -s /usr/share/maven/ref/settings-docker.xml dependency:resolve
RUN /bin/bash -c "echo 'Hello World'"

#FROM openjdk:11-jre-slim
#COPY --from=build  /bin/hello /bin/hello
#RUN addgroup -S spring && adduser -S spring -G spring
#USER spring:spring
#ARG DEPENDENCY=target/
#COPY ${DEPENDENCY}/web-backend-0.1.0.jar /app/
#COPY ${DEPENDENCY}/META-INF /app/META-INF
#COPY ${DEPENDENCY}/BOOT-INF/classes /app
#ENTRYPOINT ["java","-jar","/app/web-backend-0.1.0.jar"]
