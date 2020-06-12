FROM java:8

COPY target/bingimage-*.jar app.jar

ENTRYPOINT ["java", "-Duser.timezone=GMT+08", "-jar", "/app.jar"]