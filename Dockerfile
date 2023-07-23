FROM gradle:8.2.1-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle shadowJar --no-daemon

FROM eclipse-temurin:17-jre
RUN mkdir /app
WORKDIR /app
RUN apt-get update && \
    apt-get install --no-install-recommends -y git &&\
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*
COPY --from=build /home/gradle/src/build/libs/StreetCompleteStats-0.0.1-all.jar /app/StreetCompleteStats-all.jar
COPY icons icons
COPY update.sh update.sh
ENTRYPOINT ["/app/update.sh"]
