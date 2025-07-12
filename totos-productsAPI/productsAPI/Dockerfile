FROM openjdk:21-slim
VOLUME /tmp
COPY target/productsAPI-*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]