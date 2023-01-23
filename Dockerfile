FROM openjdk:11
EXPOSE 60005
COPY /server/target/*.jar /
ENTRYPOINT java -jar *.jar
