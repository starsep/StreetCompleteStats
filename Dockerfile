FROM gradle:8.2.1-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle shadowJar --no-daemon

FROM eclipse-temurin:17-jre
RUN mkdir /app
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/StreetCompleteStats-all.jar /app/
ENTRYPOINT ["java","-jar","/app/StreetCompleteStats-all.jar"]
