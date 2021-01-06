FROM oracle/graalvm-ce:20.1.0-java11 as graalvm
RUN gu install native-image

COPY . /home/app/open-event
WORKDIR /home/app/open-event

RUN native-image --no-server -cp server/build/libs/open-event-*-all.jar
RUN ls -lha /home/app/open-event/open-event-app

FROM frolvlad/alpine-glibc
RUN apk update && apk add libstdc++
EXPOSE 8080
COPY --from=graalvm /home/app/open-event/open-event-app /app/open-event
ENTRYPOINT ["/app/open-event"]
