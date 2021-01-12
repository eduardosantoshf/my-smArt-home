FROM openjdk:11
COPY ./target/my-smArt-home-0.0.1-SNAPSHOT.jar my-smArt-home-0.0.1-SNAPSHOT.jar
ADD wait-for-it.sh /
RUN chmod u+x ./wait-for-it.sh

CMD ["./wait-for-it.sh","rabbitmq:5672","--","java","-jar","my-smArt-home-0.0.1-SNAPSHOT.jar"]
