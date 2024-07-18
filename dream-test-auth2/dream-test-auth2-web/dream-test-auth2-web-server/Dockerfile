FROM adoptopenjdk:8-jre

RUN mkdir -p /app

WORKDIR /app

ARG JAR_FILE=build/libs/dream-boot-*-ga.jar

COPY ${JAR_FILE} dream-boot.jar

EXPOSE 9527

ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

CMD sleep 60; java -jar dream-boot.jar $JAVA_OPTS