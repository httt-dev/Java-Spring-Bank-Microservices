#Start with a base image container java runtime 
FROM openjdk:8-slim as build

#Information around who maintains the image 
MAINTAINER ngx.com

#Add the application's jar to the container
COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

#execute the application
ENTRYPOINT [ "java","-jar","/accounts-0.0.1-SNAPSHOT.jar" ]
