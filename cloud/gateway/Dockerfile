FROM openjdk:8u212-jre
MAINTAINER MrBird 852252810@qq.com

COPY ./target/gateway-3.0-RELEASE.jar /jar/gateway-3.0-RELEASE.jar
ADD agent/ /agent

ENTRYPOINT ["java", "-javaagent:/agent/skywalking-agent.jar", "-Dskywalking.agent.service_name=gateway", "-Dskywalking.collector.backend_service=skywalkingIp:11800", "-jar", "/jar/gateway-3.0-RELEASE.jar"]