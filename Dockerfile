FROM openjdk:12.0
COPY ./target/webdrivertraining-1.0-SNAPSHOT.jar /opt/webdrivertraining.jar
ENTRYPOINT ["java", "/opt/webdrivertraining.jar"]