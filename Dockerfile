FROM openjdk:13-jdk-alpine
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
ADD target/cabify-manuj243-test-0.0.1-SNAPSHOT.jar cabify-manuj243-test.jar
EXPOSE 9091
ENTRYPOINT exec java $JAVA_OPTS -jar cabify-manuj243-test.jar