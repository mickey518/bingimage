FROM maven:3.8.4-ibmjava-8-alpine AS MAVEN_BUILD

WORKDIR /build/

COPY ./maven/.m2/settings.xml /root/.m2/settings.xml

COPY ./pom.xml /build/pom.xml
COPY ./src /build/src

RUN mvn clean package

FROM airdock/oraclejdk:1.8

COPY --from=MAVEN_BUILD /build/target/bingimage-*.jar app.jar

ENTRYPOINT ["java", "-Duser.timezone=GMT+08", "-jar", "app.jar"]