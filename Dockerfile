FROM maven:3.6.3-jdk-8 AS MAVEN_BUILD

WORKDIR /build/

#COPY ./maven/.m2/settings.xml /root/.m2/settings.xml

COPY ./pom.xml /build/pom.xml
COPY ./src /build/src

RUN mvn clean package

FROM java:8

COPY --from=MAVEN_BUILD /build/target/bingimage-*.jar app.jar

ENTRYPOINT ["java", "-Duser.timezone=GMT+08", "-jar", "/app.jar"]