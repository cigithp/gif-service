FROM java:8-jdk-alpine

COPY ./target/gifservice-0.0.1-SNAPSHOT.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch gifservice-0.0.1-SNAPSHOT.jar'

EXPOSE 80

ENTRYPOINT ["java","-jar","gifservice-0.0.1-SNAPSHOT.jar"]