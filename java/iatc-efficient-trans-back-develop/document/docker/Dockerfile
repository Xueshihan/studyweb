FROM openjdk:8u272-jdk
WORKDIR /efficient_trans
COPY app.jar .
CMD ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:-OmitStackTraceInFastThrow", "-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]