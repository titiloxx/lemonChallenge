FROM openjdk:17-jdk-oraclelinux8
COPY . .
RUN chmod 744 mvnw
RUN ./mvnw install 
CMD java -jar /target/lemon-0.0.1-SNAPSHOT.jar
EXPOSE 8080
