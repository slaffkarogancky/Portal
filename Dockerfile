FROM openjdk:8-jdk-alpine
MAINTAINER kharkov.kp.gic
VOLUME /tmp
ADD target/Portal-0.0.1-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar
EXPOSE 2018