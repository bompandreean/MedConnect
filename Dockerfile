# get jdk as base
FROM openjdk:8-jdk-alpine as base
# specify in what directory will we work ( in container)
WORKDIR /app
# copy the properties file that start the app - will not start without
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
# command for mvnw "Permission denied" error in github
RUN chmod +x mvnw
# let maven resolve all dependencies from pom
RUN ./mvnw dependency:resolve
# copy the rest of the app
COPY src/ src
#expose a port to acces app
EXPOSE 8080
# run your app
CMD ./mvnw spring-boot:run



