FROM amd64/amazoncorretto:17
WORKDIR /app
COPY ./build/libs/server-0.0.1-SNAPSHOT.jar /app/NUTSHELL.jar
CMD ["java", "-Duser.timezone=Asia/Seoul",  "-jar", "-Dspring.profiles.active=dev", "NUTSHELL.jar"]
