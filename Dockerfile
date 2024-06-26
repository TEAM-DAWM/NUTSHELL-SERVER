FROM amd64/amazoncorretto:17
WORKDIR /app
COPY ./build/libs/server-0.0.1-SNAPSHOT.jar /app/NUT-SHELL.jar
CMD ["sh", "-c", "java -Duser.timezone=Asia/Seoul -jar -Dspring.profiles.active=dev NUT-SHELL.jar > console.log 2>&1"]
