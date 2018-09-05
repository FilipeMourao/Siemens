FROM gradle:4.2.1-jdk8-alpine as builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle war --stacktrace

FROM jboss/wildfly
COPY --from=builder /home/gradle/src/build/libs/server.war /opt/jboss/wildfly/standalone/deployments/