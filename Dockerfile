FROM openjdk:8-jre-alpine
COPY build/libs/db_service-1.0.0.jar /db_service.jar
CMD ["/usr/bin/java", "-jar", "/db_service.jar"]
