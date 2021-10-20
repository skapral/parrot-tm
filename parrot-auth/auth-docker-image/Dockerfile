FROM adoptopenjdk:11-jre-hotspot

ARG version
LABEL VERSION=$version

RUN mkdir /opt/service && chmod a+rw /opt/service
COPY target/dependency/auth-service-$version.jar /opt/service
RUN mv /opt/service/auth-service-$version.jar /opt/service/auth-service.jar

WORKDIR /opt/service
EXPOSE 8080

CMD exec java -jar ./auth-service.jar