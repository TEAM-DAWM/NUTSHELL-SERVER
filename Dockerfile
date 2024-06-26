FROM amd64/amazoncorretto:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app/NUTSHELL.jar
CMD ["java", "-Duser.timezone=Asia/Seoul",  "-jar", "-Dspring.profiles.active=dev", "/app/NUTSHELL.jar"]
